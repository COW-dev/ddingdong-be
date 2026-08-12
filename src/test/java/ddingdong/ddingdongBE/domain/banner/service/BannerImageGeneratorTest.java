package ddingdong.ddingdongBE.domain.banner.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BannerImageGeneratorTest {

    private static final String TITLE_TEXT = "이달의 피드 : 컴퓨터공학과 동아리 축하드립니다!";
    private static final String SUBTITLE_TEXT = "6월의 피드는 '동아리 피드'에서 확인하실 수 있습니다.";
    private static final String BOLD_FONT_PATH = "fonts/Pretendard-Bold.otf";
    private static final String MEDIUM_FONT_PATH = "fonts/Pretendard-Medium.otf";

    private BannerImageGenerator bannerImageGenerator;

    @TempDir
    Path fontDirectory;

    @BeforeAll
    static void enableHeadless() {
        // EB(Elastic Beanstalk) 운영 환경과 동일하게 headless AWT로 폰트 서브시스템을 초기화한다.
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    void setUp() {
        bannerImageGenerator = new BannerImageGenerator(fontDirectory.toString());
        bannerImageGenerator.init();
    }

    @DisplayName("제목에 사용하는 굵은 폰트는 제목의 모든 한글 글리프를 가지고 있다")
    @Test
    void boldFontCanDisplayAllKoreanGlyphsInTitle() throws Exception {
        Font boldFont = loadedFont(BOLD_FONT_PATH);

        assertThat(boldFont.canDisplayUpTo(TITLE_TEXT)).isEqualTo(-1);
    }

    @DisplayName("부제목에 사용하는 중간 굵기 폰트는 부제목의 모든 한글 글리프를 가지고 있다")
    @Test
    void mediumFontCanDisplayAllKoreanGlyphsInSubtitle() throws Exception {
        Font mediumFont = loadedFont(MEDIUM_FONT_PATH);

        assertThat(mediumFont.canDisplayUpTo(SUBTITLE_TEXT)).isEqualTo(-1);
    }

    @DisplayName("폰트는 지정된 경로에 풀리며, OS 정리 대상인 JDK 임시 폰트파일을 만들지 않는다")
    @Test
    void fontsAreExtractedIntoGivenDirectoryWithoutJdkTempFiles() throws Exception {
        // given - Font.createFont(int, InputStream) 이 만드는 임시파일("+~JF*.tmp") 목록을 미리 확보한다
        Path systemTempDirectory = Path.of(System.getProperty("java.io.tmpdir"));
        long tempFontFilesBefore = countJdkTempFontFiles(systemTempDirectory);

        // when
        Path anotherFontDirectory = Files.createDirectory(fontDirectory.resolve("reloaded"));
        new BannerImageGenerator(anotherFontDirectory.toString()).init();

        // then
        assertThat(Files.exists(anotherFontDirectory.resolve("Pretendard-Bold.otf"))).isTrue();
        assertThat(Files.exists(anotherFontDirectory.resolve("Pretendard-Medium.otf"))).isTrue();
        assertThat(countJdkTempFontFiles(systemTempDirectory)).isEqualTo(tempFontFilesBefore);
    }

    private long countJdkTempFontFiles(Path directory) throws Exception {
        try (var files = Files.list(directory)) {
            return files.filter(file -> file.getFileName().toString().startsWith("+~JF")).count();
        }
    }

    @DisplayName("제목 폰트가 런타임에 해제되어도 제목의 한글이 네모로 깨지지 않는다")
    @Test
    void reloadsTitleFontWhenItIsReleasedAtRuntime() throws Exception {
        // given - 어떤 문자도 표현하지 못하는 폰트로 바꿔 폰트가 해제된 상황을 만든다
        overrideLoadedFont(BOLD_FONT_PATH, new UndisplayableFont());

        // when
        byte[] result = bannerImageGenerator.generateWebBannerImage("테스트동아리", createTestLogo(), "학술", 2);

        // then
        assertThat(loadedFont(BOLD_FONT_PATH)).isNotInstanceOf(UndisplayableFont.class);
        assertThat(loadedFont(BOLD_FONT_PATH).canDisplayUpTo(TITLE_TEXT)).isEqualTo(-1);
        assertKoreanTextRendered(readImage(result), 360, 70, 450, 55);
    }

    @DisplayName("부제목 폰트가 런타임에 해제되어도 부제목의 한글이 네모로 깨지지 않는다")
    @Test
    void reloadsSubtitleFontWhenItIsReleasedAtRuntime() throws Exception {
        // given
        overrideLoadedFont(MEDIUM_FONT_PATH, new UndisplayableFont());

        // when
        byte[] result = bannerImageGenerator.generateWebBannerImage("테스트동아리", createTestLogo(), "학술", 2);

        // then
        assertThat(loadedFont(MEDIUM_FONT_PATH)).isNotInstanceOf(UndisplayableFont.class);
        assertThat(loadedFont(MEDIUM_FONT_PATH).canDisplayUpTo(SUBTITLE_TEXT)).isEqualTo(-1);
        assertThat(result.length).isGreaterThan(0);
    }

    @DisplayName("폰트를 재로드해도 이미 추출해둔 폰트 파일은 교체되지 않는다")
    @Test
    void keepsExtractedFontFileIntactOnReload() throws Exception {
        // given - 렌더링 중인 다른 스레드가 이 파일을 backing file 로 쓰고 있을 수 있다
        Path extractedFont = extractedFontFile(BOLD_FONT_PATH);
        FileTime modifiedTimeBefore = Files.getLastModifiedTime(extractedFont);
        long fontFilesBefore = countFontFiles();
        overrideLoadedFont(BOLD_FONT_PATH, new UndisplayableFont());

        // when
        bannerImageGenerator.generateWebBannerImage("테스트동아리", createTestLogo(), "학술", 2);

        // then
        assertThat(extractedFontFile(BOLD_FONT_PATH)).isEqualTo(extractedFont);
        assertThat(Files.getLastModifiedTime(extractedFont)).isEqualTo(modifiedTimeBefore);
        assertThat(countFontFiles()).isEqualTo(fontFilesBefore);
    }

    @DisplayName("추출해둔 폰트 파일이 손상되면 덮어쓰지 않고 새 파일로 추출해 한글을 정상 렌더링한다")
    @Test
    void extractsNewFontFileWithoutOverwritingCorruptedOne() throws Exception {
        // given
        Path corruptedFont = extractedFontFile(BOLD_FONT_PATH);
        byte[] corruptedContent = "not a font".getBytes(StandardCharsets.UTF_8);
        Files.write(corruptedFont, corruptedContent);
        long fontFilesBefore = countFontFiles();
        overrideLoadedFont(BOLD_FONT_PATH, new UndisplayableFont());

        // when
        byte[] result = bannerImageGenerator.generateWebBannerImage("테스트동아리", createTestLogo(), "학술", 2);

        // then
        assertThat(Files.readAllBytes(corruptedFont)).isEqualTo(corruptedContent);
        assertThat(extractedFontFile(BOLD_FONT_PATH)).isNotEqualTo(corruptedFont);
        assertThat(countFontFiles()).isEqualTo(fontFilesBefore + 1);
        assertKoreanTextRendered(readImage(result), 360, 70, 450, 55);
    }

    private long countFontFiles() throws Exception {
        try (var files = Files.list(fontDirectory)) {
            return files.filter(Files::isRegularFile).count();
        }
    }

    @SuppressWarnings("unchecked")
    private Path extractedFontFile(String fontPath) throws Exception {
        Field field = BannerImageGenerator.class.getDeclaredField("extractedFontFiles");
        field.setAccessible(true);
        return ((Map<String, Path>) field.get(bannerImageGenerator)).get(fontPath);
    }

    // 논리 폰트(Dialog)는 실행 환경에 한글 폰트가 있으면 한글을 표현할 수 있어
    // 재로드 경로가 실행되지 않는다. 어떤 문자도 표현하지 못하는 폰트로 상황을 고정한다.
    private static final class UndisplayableFont extends Font {

        private UndisplayableFont() {
            super(Font.DIALOG, Font.PLAIN, 1);
        }

        @Override
        public int canDisplayUpTo(String str) {
            return 0;
        }
    }

    private void overrideLoadedFont(String fontPath, Font font) throws Exception {
        loadedFonts().put(fontPath, font);
    }

    private Font loadedFont(String fontPath) throws Exception {
        return loadedFonts().get(fontPath);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Font> loadedFonts() throws Exception {
        Field field = BannerImageGenerator.class.getDeclaredField("loadedFonts");
        field.setAccessible(true);
        return (Map<String, Font>) field.get(bannerImageGenerator);
    }

    @DisplayName("웹 배너 이미지가 정상적으로 생성된다")
    @Test
    void generateWebBannerImage_success() throws IOException {
        // given
        BufferedImage logo = createTestLogo();

        // when
        byte[] result = bannerImageGenerator.generateWebBannerImage("테스트동아리", logo, "학술", 2);

        // then
        assertThat(result).isNotNull();
        assertThat(result.length).isGreaterThan(0);
        assertKoreanTextRendered(readImage(result), 360, 70, 450, 55);
    }

    @DisplayName("웹 배너 제목은 Bold 굵기로 렌더링된다")
    @Test
    void generateWebBannerImage_titleRenderedBold() throws IOException {
        // given
        BufferedImage logo = createTestLogo();

        // when
        byte[] result = bannerImageGenerator.generateWebBannerImage("테스트동아리", logo, "학술", 2);

        // then
        assertThat(countDarkTextPixels(readImage(result), 780, 120, 1_000, 100)).isGreaterThan(20_000);
    }

    @DisplayName("모바일 배너 이미지가 정상적으로 생성된다")
    @Test
    void generateMobileBannerImage_success() throws IOException {
        // given
        BufferedImage logo = createTestLogo();

        // when
        byte[] result = bannerImageGenerator.generateMobileBannerImage("테스트동아리", logo, "학술", 2);

        // then
        assertThat(result).isNotNull();
        assertThat(result.length).isGreaterThan(0);
        assertKoreanTextRendered(readImage(result), 75, 230, 535, 55);
    }

    @DisplayName("로고가 null이어도 배너 이미지가 정상적으로 생성된다")
    @Test
    void generateWebBannerImage_nullLogo() {
        // when
        byte[] result = bannerImageGenerator.generateWebBannerImage("테스트동아리", null, "봉사", 3);

        // then
        assertThat(result).isNotNull();
        assertThat(result.length).isGreaterThan(0);
    }

    @DisplayName("알 수 없는 카테고리로도 배너가 정상 생성된다")
    @Test
    void generateWebBannerImage_unknownCategory() {
        // given
        BufferedImage logo = createTestLogo();

        // when
        byte[] result = bannerImageGenerator.generateWebBannerImage("테스트동아리", logo, "알수없는분과", 1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.length).isGreaterThan(0);
    }

    private BufferedImage createTestLogo() {
        BufferedImage logo = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = logo.createGraphics();
        graphics.setColor(Color.BLUE);
        graphics.fillOval(0, 0, 100, 100);
        graphics.dispose();
        return logo;
    }

    private BufferedImage readImage(byte[] imageBytes) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        assertThat(image).isNotNull();
        return image;
    }

    private void assertKoreanTextRendered(BufferedImage image, int startX, int startY, int width, int height) {
        Color backgroundColor = new Color(image.getRGB(0, 0), true);
        int textPixelCount = countTextPixels(image, startX, startY, width, height);
        boolean[] seenGrayScale = new boolean[256];
        int uniqueTextColorCount = 0;

        for (int y = startY; y < startY + height; y++) {
            for (int x = startX; x < startX + width; x++) {
                Color pixelColor = new Color(image.getRGB(x, y), true);
                if (isDifferentColor(pixelColor, backgroundColor)) {
                    int grayScale = (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue()) / 3;
                    if (!seenGrayScale[grayScale]) {
                        seenGrayScale[grayScale] = true;
                        uniqueTextColorCount++;
                    }
                }
            }
        }

        assertThat(textPixelCount).isGreaterThan(1_000);
        assertThat(uniqueTextColorCount).isGreaterThan(10);
    }

    private int countTextPixels(BufferedImage image, int startX, int startY, int width, int height) {
        Color backgroundColor = new Color(image.getRGB(0, 0), true);
        int textPixelCount = 0;

        for (int y = startY; y < startY + height; y++) {
            for (int x = startX; x < startX + width; x++) {
                Color pixelColor = new Color(image.getRGB(x, y), true);
                if (isDifferentColor(pixelColor, backgroundColor)) {
                    textPixelCount++;
                }
            }
        }

        return textPixelCount;
    }

    private int countDarkTextPixels(BufferedImage image, int startX, int startY, int width, int height) {
        int textPixelCount = 0;

        for (int y = startY; y < startY + height; y++) {
            for (int x = startX; x < startX + width; x++) {
                Color pixelColor = new Color(image.getRGB(x, y), true);
                if (pixelColor.getRed() < 80 && pixelColor.getGreen() < 90 && pixelColor.getBlue() < 110) {
                    textPixelCount++;
                }
            }
        }

        return textPixelCount;
    }

    private boolean isDifferentColor(Color source, Color target) {
        int redDiff = Math.abs(source.getRed() - target.getRed());
        int greenDiff = Math.abs(source.getGreen() - target.getGreen());
        int blueDiff = Math.abs(source.getBlue() - target.getBlue());
        return redDiff + greenDiff + blueDiff > 30;
    }
}
