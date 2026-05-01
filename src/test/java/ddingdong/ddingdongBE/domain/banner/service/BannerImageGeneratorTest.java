package ddingdong.ddingdongBE.domain.banner.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BannerImageGeneratorTest {

    private BannerImageGenerator bannerImageGenerator;

    @BeforeEach
    void setUp() {
        bannerImageGenerator = new BannerImageGenerator();
        bannerImageGenerator.init();
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
