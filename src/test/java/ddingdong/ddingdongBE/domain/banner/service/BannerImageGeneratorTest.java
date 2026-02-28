package ddingdong.ddingdongBE.domain.banner.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
    void generateWebBannerImage_success() {
        // given
        BufferedImage logo = createTestLogo();

        // when
        byte[] result = bannerImageGenerator.generateWebBannerImage("테스트동아리", logo, "학술", 2);

        // then
        assertThat(result).isNotNull();
        assertThat(result.length).isGreaterThan(0);
    }

    @DisplayName("모바일 배너 이미지가 정상적으로 생성된다")
    @Test
    void generateMobileBannerImage_success() {
        // given
        BufferedImage logo = createTestLogo();

        // when
        byte[] result = bannerImageGenerator.generateMobileBannerImage("테스트동아리", logo, "학술", 2);

        // then
        assertThat(result).isNotNull();
        assertThat(result.length).isGreaterThan(0);
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
}
