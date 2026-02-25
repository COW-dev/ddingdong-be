package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.entity.ClubCategoryColor;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BannerImageGenerator {

    private static final int WEB_WIDTH = 1032;
    private static final int WEB_HEIGHT = 200;
    private static final int MOBILE_WIDTH = 342;
    private static final int MOBILE_HEIGHT = 225;
    private static final int SCALE = 2;

    private static final int WEB_LOGO_SIZE = 160;
    private static final int WEB_LOGO_MARGIN = 40;
    private static final int MOBILE_LOGO_SIZE = 80;

    private static final String BOLD_FONT_PATH = "fonts/Pretendard-Bold.otf";
    private static final String MEDIUM_FONT_PATH = "fonts/Pretendard-Medium.otf";

    private Font boldBaseFont;
    private Font mediumBaseFont;

    public byte[] generateWebBannerImage(String clubName, BufferedImage clubLogo, String category, int month) {
        BufferedImage banner = new BufferedImage(WEB_WIDTH * SCALE, WEB_HEIGHT * SCALE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = banner.createGraphics();

        try {
            setupRenderingHints(graphics);
            graphics.scale(SCALE, SCALE);
            drawBackground(graphics, category, WEB_WIDTH, WEB_HEIGHT);

            int logoY = (WEB_HEIGHT - WEB_LOGO_SIZE) / 2;
            drawClubLogo(graphics, clubLogo, WEB_LOGO_MARGIN, logoY, WEB_LOGO_SIZE);
            drawWebTexts(graphics, clubName, month);
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

            int logoX = (MOBILE_WIDTH - MOBILE_LOGO_SIZE) / 2;
            int logoY = 20;
            drawClubLogo(graphics, clubLogo, logoX, logoY, MOBILE_LOGO_SIZE);
            drawMobileTexts(graphics, clubName, month, logoY + MOBILE_LOGO_SIZE);
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

        Graphics2D logoGraphics = (Graphics2D) graphics.create();
        logoGraphics.setClip(new Ellipse2D.Double(logoX, logoY, logoSize, logoSize));
        logoGraphics.setComposite(AlphaComposite.SrcOver);
        logoGraphics.drawImage(clubLogo, logoX, logoY, logoSize, logoSize, null);
        logoGraphics.dispose();
    }

    private void drawWebTexts(Graphics2D graphics, String clubName, int month) {
        int textStartX = WEB_LOGO_MARGIN + WEB_LOGO_SIZE + 30;

        // PC/Title/Bold1: Pretendard Bold 36px, letter-spacing -1%
        Font mainFont = createStyledFont(getBoldFont(), Font.BOLD, 36f, -0.01f);
        graphics.setFont(mainFont);
        graphics.setColor(new Color(33, 33, 33));
        FontMetrics mainMetrics = graphics.getFontMetrics();
        String mainText = "이달의 피드 : " + clubName + " 축하드립니다!";
        int mainTextY = (WEB_HEIGHT / 2) + (mainMetrics.getAscent() - mainMetrics.getDescent()) / 2 - 12;
        graphics.drawString(mainText, textStartX, mainTextY);

        // PC/Body/Medium2: Pretendard Medium 16px, letter-spacing 1%
        Font subFont = createStyledFont(getMediumFont(), Font.PLAIN, 16f, 0.01f);
        graphics.setFont(subFont);
        graphics.setColor(new Color(100, 100, 100));
        graphics.drawString(month + "월의 피드는 '동아리 피드'에서 확인하실 수 있습니다.",
                textStartX, mainTextY + 30);
    }

    private void drawMobileTexts(Graphics2D graphics, String clubName, int month, int logoBottom) {
        // Mobile/Title: Pretendard Bold 18px, centered
        Font mainFont = createStyledFont(getBoldFont(), Font.BOLD, 18f, -0.01f);
        graphics.setFont(mainFont);
        graphics.setColor(new Color(33, 33, 33));
        FontMetrics mainMetrics = graphics.getFontMetrics();
        String mainText = "이달의 피드 : " + clubName + " 축하드립니다!";
        int mainX = (MOBILE_WIDTH - mainMetrics.stringWidth(mainText)) / 2;
        int mainY = logoBottom + 35;
        graphics.drawString(mainText, mainX, mainY);

        // Mobile/Sub: Pretendard Medium 12px, centered
        Font subFont = createStyledFont(getMediumFont(), Font.PLAIN, 12f, 0.01f);
        graphics.setFont(subFont);
        graphics.setColor(new Color(100, 100, 100));
        FontMetrics subMetrics = graphics.getFontMetrics();
        String subText = month + "월의 피드는 '동아리 피드'에서 확인하실 수 있습니다.";
        int subX = (MOBILE_WIDTH - subMetrics.stringWidth(subText)) / 2;
        graphics.drawString(subText, subX, mainY + 22);
    }

    private Font createStyledFont(Font baseFont, int style, float size, float tracking) {
        Font sized = baseFont.deriveFont(style, size);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, tracking);
        return sized.deriveFont(attributes);
    }

    private Font getBoldFont() {
        if (boldBaseFont != null) {
            return boldBaseFont;
        }
        boldBaseFont = loadFont(BOLD_FONT_PATH);
        return boldBaseFont;
    }

    private Font getMediumFont() {
        if (mediumBaseFont != null) {
            return mediumBaseFont;
        }
        mediumBaseFont = loadFont(MEDIUM_FONT_PATH);
        return mediumBaseFont;
    }

    private Font loadFont(String path) {
        try (InputStream fontStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (fontStream != null) {
                return Font.createFont(Font.TRUETYPE_FONT, fontStream);
            }
        } catch (Exception e) {
            log.warn("커스텀 폰트 로드 실패 ({}), 기본 폰트 사용: {}", path, e.getMessage());
        }
        return new Font("SansSerif", Font.BOLD, 36);
    }

    private byte[] toPngBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("배너 이미지 변환 중 오류가 발생했습니다.", e);
        }
    }
}
