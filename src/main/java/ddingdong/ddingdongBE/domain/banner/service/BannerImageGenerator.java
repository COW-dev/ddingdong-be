package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.common.exception.BannerException.BannerImageGenerationException;
import ddingdong.ddingdongBE.domain.banner.entity.ClubCategoryColor;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.annotation.PostConstruct;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BannerImageGenerator {

    private static final int WEB_WIDTH = 1080;
    private static final int WEB_HEIGHT = 200;
    private static final int MOBILE_WIDTH = 342;
    private static final int MOBILE_HEIGHT = 225;
    private static final int SCALE = 2;
    private static final int BORDER_RADIUS = 12;

    private static final int LOGO_SIZE = 160;
    private static final int CONTENT_GAP = 24;
    private static final int TEXT_BLOCK_WIDTH = 472;
    private static final int MOBILE_LOGO_SIZE = 80;

    private static final String BOLD_FONT_PATH = "fonts/Pretendard-Bold.otf";
    private static final String MEDIUM_FONT_PATH = "fonts/Pretendard-Medium.otf";

    // 배너는 매월 1일에만 생성되므로, 기동 시 로드한 폰트가 수 주간 유휴 상태로 남는다.
    // 그 사이 외부 요인으로 폰트가 해제되면 렌더링 직전에 재로드하므로 폰트 경로별로 보관한다.
    private final Map<String, Font> loadedFonts = new ConcurrentHashMap<>();

    // 재로드는 폰트 파일을 다시 쓰는 작업이라, 같은 폰트에 대해 동시에 일어나면
    // 한쪽이 읽는 중인 파일을 다른 쪽이 교체할 수 있다. 폰트 단위로 직렬화한다.
    private final Map<String, Object> fontLoadLocks = new ConcurrentHashMap<>();

    private final Path fontDirectory;

    // JDK가 관리하는 임시 디렉토리(/tmp)가 아니라 애플리케이션이 소유한 경로에 폰트를 풀어둔다.
    // 배포마다 새로 만들어지는 앱 디렉토리이므로 OS의 임시파일 정리 대상이 되지 않는다.
    public BannerImageGenerator(@Value("${banner.font-directory:}") String fontDirectory) {
        this.fontDirectory = fontDirectory.isBlank()
                ? Paths.get(System.getProperty("user.dir"), "fonts")
                : Paths.get(fontDirectory);
    }

    @PostConstruct
    void init() {
        loadedFonts.put(BOLD_FONT_PATH, loadFont(BOLD_FONT_PATH));
        loadedFonts.put(MEDIUM_FONT_PATH, loadFont(MEDIUM_FONT_PATH));
    }

    public byte[] generateWebBannerImage(String clubName, BufferedImage clubLogo, String category, int month) {
        BufferedImage banner = new BufferedImage(WEB_WIDTH * SCALE, WEB_HEIGHT * SCALE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = banner.createGraphics();

        try {
            setupRenderingHints(graphics);
            graphics.scale(SCALE, SCALE);

            // 라운드 코너 배경
            RoundRectangle2D roundedRect = new RoundRectangle2D.Double(
                    0, 0, WEB_WIDTH, WEB_HEIGHT, BORDER_RADIUS * 2, BORDER_RADIUS * 2);
            graphics.setClip(roundedRect);
            drawBackground(graphics, category, WEB_WIDTH, WEB_HEIGHT);

            // 콘텐츠 중앙 배치: [로고160] [gap24] [텍스트472]
            int contentWidth = LOGO_SIZE + CONTENT_GAP + TEXT_BLOCK_WIDTH;
            int contentStartX = (WEB_WIDTH - contentWidth) / 2;

            int logoY = (WEB_HEIGHT - LOGO_SIZE) / 2;
            drawClubLogo(graphics, clubLogo, contentStartX, logoY, LOGO_SIZE);

            int textX = contentStartX + LOGO_SIZE + CONTENT_GAP;
            drawWebTexts(graphics, clubName, month, textX);
        } finally {
            graphics.dispose();
        }

        return toPngBytes(banner);
    }

    public byte[] generateMobileBannerImage(String clubName, BufferedImage clubLogo, String category, int month) {
        BufferedImage banner = new BufferedImage(MOBILE_WIDTH * SCALE, MOBILE_HEIGHT * SCALE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = banner.createGraphics();

        try {
            setupRenderingHints(graphics);
            graphics.scale(SCALE, SCALE);
            drawBackground(graphics, category, MOBILE_WIDTH, MOBILE_HEIGHT);

            // 전체 콘텐츠 높이 계산: 로고 + 간격 + 메인텍스트 + 서브텍스트
            int contentGap = 12;
            int mainLineHeight = 22;
            int subLineHeight = 18;
            int totalContentHeight = MOBILE_LOGO_SIZE + contentGap + mainLineHeight + subLineHeight;
            int contentStartY = (MOBILE_HEIGHT - totalContentHeight) / 2;

            int logoX = (MOBILE_WIDTH - MOBILE_LOGO_SIZE) / 2;
            drawClubLogo(graphics, clubLogo, logoX, contentStartY, MOBILE_LOGO_SIZE);
            drawMobileTexts(graphics, clubName, month, contentStartY + MOBILE_LOGO_SIZE + contentGap);
        } finally {
            graphics.dispose();
        }

        return toPngBytes(banner);
    }

    private void setupRenderingHints(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    private void drawBackground(Graphics2D graphics, String category, int width, int height) {
        ClubCategoryColor categoryColor = ClubCategoryColor.fromCategory(category);
        Color backgroundColor = Color.decode(categoryColor.getHexColor());
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
    }

    private void drawClubLogo(Graphics2D graphics, BufferedImage clubLogo, int logoX, int logoY, int logoSize) {
        if (clubLogo == null) {
            return;
        }

        // 원본 비율 유지하면서 logoSize 안에 맞추기 (짤림 방지)
        int originalWidth = clubLogo.getWidth();
        int originalHeight = clubLogo.getHeight();
        double scale = Math.min((double) logoSize / originalWidth, (double) logoSize / originalHeight);
        int drawWidth = (int) (originalWidth * scale);
        int drawHeight = (int) (originalHeight * scale);

        int drawX = logoX + (logoSize - drawWidth) / 2;
        int drawY = logoY + (logoSize - drawHeight) / 2;

        graphics.drawImage(clubLogo, drawX, drawY, drawWidth, drawHeight, null);
    }

    private void drawWebTexts(Graphics2D graphics, String clubName, int month, int textX) {
        // 텍스트 블록 높이: main(40) + gap(4) + sub(24) = 68
        int textBlockHeight = 68;
        int textStartY = (WEB_HEIGHT - textBlockHeight) / 2;

        // Use the Pretendard-Bold font face without applying Java synthetic bold style.
        String mainText = "이달의 피드 : " + clubName + " 축하드립니다!";
        Font mainFont = titleFont(36f, mainText);
        graphics.setFont(mainFont);
        graphics.setColor(Color.decode("#1F2937"));
        FontMetrics mainMetrics = graphics.getFontMetrics();
        int mainY = textStartY + mainMetrics.getAscent();
        graphics.drawString(mainText, textX, mainY);

        // PC/Body/Medium2: Pretendard Medium 16px, line-height 24px
        String subText = month + "월의 피드는 '동아리 피드'에서 확인하실 수 있습니다.";
        Font subFont = bodyFont(16f, subText);
        graphics.setFont(subFont);
        graphics.setColor(Color.decode("#6B7280"));
        FontMetrics subMetrics = graphics.getFontMetrics();
        int subY = textStartY + 40 + 4 + subMetrics.getAscent();
        graphics.drawString(subText, textX, subY);
    }

    private void drawMobileTexts(Graphics2D graphics, String clubName, int month, int textStartY) {
        String mainText = "이달의 피드 : " + clubName + " 축하드립니다!";
        Font mainFont = titleFont(18f, mainText);
        graphics.setFont(mainFont);
        graphics.setColor(new Color(33, 33, 33));
        FontMetrics mainMetrics = graphics.getFontMetrics();
        int mainX = (MOBILE_WIDTH - mainMetrics.stringWidth(mainText)) / 2;
        int mainY = textStartY + mainMetrics.getAscent();
        graphics.drawString(mainText, mainX, mainY);

        // Mobile/Sub: Pretendard Medium 12px, centered
        String subText = month + "월의 피드는 '동아리 피드'에서 확인하실 수 있습니다.";
        Font subFont = bodyFont(12f, subText);
        graphics.setFont(subFont);
        graphics.setColor(new Color(100, 100, 100));
        FontMetrics subMetrics = graphics.getFontMetrics();
        int subX = (MOBILE_WIDTH - subMetrics.stringWidth(subText)) / 2;
        graphics.drawString(subText, subX, mainY + 20);
    }

    // 로드한 폰트 인스턴스의 내부 스타일을 유지한 채 크기만 조정한다.
    // style(PLAIN) 지정과 TextAttribute.TRACKING(속성 기반 레이아웃)은
    // headless Linux AWT에서 폰트를 이름/스타일로 재탐색하게 만들어
    // 한글 글리프가 없는 fallback 폰트로 치환(제목 tofu)되므로 사용하지 않는다.
    private Font createStyledFont(Font baseFont, float size) {
        return baseFont.deriveFont(size);
    }

    // Font.createFont(int, InputStream) 은 폰트를 java.io.tmpdir 의 임시파일로 복사한 뒤
    // 글리프를 그 파일에서 지연 로딩한다. 운영(EB/Amazon Linux)에서는 임시파일 정리 데몬이
    // 장기간 미접근 상태인 이 파일을 삭제하고, 그 뒤 렌더링하면 JDK가 폰트를 조용히 해제한 채
    // 이름으로 재조회해 한글 글리프가 없는 fallback 폰트로 그린다(제목 tofu).
    // 따라서 앱이 소유한 경로에 폰트를 풀어두고 File 오버로드로 로드한다.
    private Font loadFont(String path) {
        // 파일을 쓰는 동안 다른 스레드가 같은 파일을 읽거나 교체하지 못하도록 폰트 단위로 잠근다.
        synchronized (fontLoadLocks.computeIfAbsent(path, key -> new Object())) {
            try (InputStream fontStream = getClass().getClassLoader().getResourceAsStream(path)) {
                if (fontStream == null) {
                    throw new BannerImageGenerationException();
                }
                Path extractedFont = extractFont(fontStream, path);
                Font font = Font.createFont(Font.TRUETYPE_FONT, extractedFont.toFile());
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                return font;
            } catch (BannerImageGenerationException e) {
                throw e;
            } catch (Exception e) {
                log.error("커스텀 폰트 로드 실패 ({}): {}", path, e.getMessage());
                throw new BannerImageGenerationException();
            }
        }
    }

    // 사용 중인 폰트 파일을 직접 덮어쓰면 읽는 쪽이 깨진 파일을 보게 되므로,
    // 임시 파일에 먼저 쓰고 같은 디렉토리 안에서 원자적으로 옮긴다.
    private Path extractFont(InputStream fontStream, String path) throws IOException {
        Files.createDirectories(fontDirectory);
        String fontFileName = Paths.get(path).getFileName().toString();
        Path extractedFont = fontDirectory.resolve(fontFileName);
        Path temporaryFont = fontDirectory.resolve(fontFileName + ".tmp");

        Files.copy(fontStream, temporaryFont, StandardCopyOption.REPLACE_EXISTING);
        try {
            Files.move(temporaryFont, extractedFont, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporaryFont, extractedFont, StandardCopyOption.REPLACE_EXISTING);
        }
        return extractedFont;
    }

    // 폰트가 해제되면 예외 없이 fallback 폰트로 그려져 배너가 조용히 깨진다.
    // 그리기 직전에 실제로 해당 문구를 표현할 수 있는지 확인하고, 불가능하면 재로드한다.
    private Font usableFont(String path, String text) {
        Font cachedFont = loadedFonts.get(path);
        if (canDisplayFully(cachedFont, text)) {
            return cachedFont;
        }

        synchronized (fontLoadLocks.computeIfAbsent(path, key -> new Object())) {
            // 잠금을 기다리는 동안 다른 스레드가 이미 재로드했을 수 있다.
            Font currentFont = loadedFonts.get(path);
            if (canDisplayFully(currentFont, text)) {
                return currentFont;
            }
            log.error("배너 폰트가 런타임에 해제되어 재로드합니다 ({})", path);

            Font reloadedFont = loadFont(path);
            if (!canDisplayFully(reloadedFont, text)) {
                log.warn("재로드 후에도 폰트가 표현할 수 없는 문자가 있습니다 ({}): {}", path, text);
            }
            loadedFonts.put(path, reloadedFont);
            return reloadedFont;
        }
    }

    private boolean canDisplayFully(Font font, String text) {
        return font != null && font.canDisplayUpTo(text) < 0;
    }

    private Font titleFont(float size, String text) {
        return createStyledFont(usableFont(BOLD_FONT_PATH, text), size);
    }

    private Font bodyFont(float size, String text) {
        return createStyledFont(usableFont(MEDIUM_FONT_PATH, text), size);
    }

    private byte[] toPngBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new BannerImageGenerationException();
        }
    }
}
