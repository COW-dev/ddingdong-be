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
import java.awt.geom.RoundRectangle2D;
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

    private Font boldBaseFont;
    private Font mediumBaseFont;

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

    private static final double LOGO_PADDING_RATIO = 0.15;

    private void drawClubLogo(Graphics2D graphics, BufferedImage clubLogo, int logoX, int logoY, int logoSize) {
        if (clubLogo == null) {
            return;
        }

        // 흰색 원형 배경
        graphics.setColor(Color.WHITE);
        graphics.fill(new Ellipse2D.Double(logoX, logoY, logoSize, logoSize));

        // 로고를 원 안에 패딩 적용하여 축소 배치 (짤림 방지)
        int padding = (int) (logoSize * LOGO_PADDING_RATIO);
        int innerSize = logoSize - padding * 2;

        // 원본 비율 유지하면서 innerSize 안에 맞추기
        int originalWidth = clubLogo.getWidth();
        int originalHeight = clubLogo.getHeight();
        double scale = Math.min((double) innerSize / originalWidth, (double) innerSize / originalHeight);
        int drawWidth = (int) (originalWidth * scale);
        int drawHeight = (int) (originalHeight * scale);

        int drawX = logoX + (logoSize - drawWidth) / 2;
        int drawY = logoY + (logoSize - drawHeight) / 2;

        // 원형 클리핑 후 로고 그리기
        Graphics2D logoGraphics = (Graphics2D) graphics.create();
        logoGraphics.setClip(new Ellipse2D.Double(logoX, logoY, logoSize, logoSize));
        logoGraphics.setComposite(AlphaComposite.SrcOver);
        logoGraphics.drawImage(clubLogo, drawX, drawY, drawWidth, drawHeight, null);
        logoGraphics.dispose();
    }

    private void drawWebTexts(Graphics2D graphics, String clubName, int month, int textX) {
        // 텍스트 블록 높이: main(40) + gap(4) + sub(24) = 68
        int textBlockHeight = 68;
        int textStartY = (WEB_HEIGHT - textBlockHeight) / 2;

        // PC/Title/Bold1: Pretendard Bold 36px, line-height 40px, letter-spacing -1%
        Font mainFont = createStyledFont(getBoldFont(), Font.BOLD, 36f, -0.01f);
        graphics.setFont(mainFont);
        graphics.setColor(Color.decode("#1F2937"));
        FontMetrics mainMetrics = graphics.getFontMetrics();
        String mainText = "이달의 피드 : " + clubName + " 축하드립니다!";
        int mainY = textStartY + mainMetrics.getAscent();
        graphics.drawString(mainText, textX, mainY);

        // PC/Body/Medium2: Pretendard Medium 16px, line-height 24px, letter-spacing 1%
        Font subFont = createStyledFont(getMediumFont(), Font.PLAIN, 16f, 0.01f);
        graphics.setFont(subFont);
        graphics.setColor(Color.decode("#6B7280"));
        FontMetrics subMetrics = graphics.getFontMetrics();
        String subText = month + "월의 피드는 '동아리 피드'에서 확인하실 수 있습니다.";
        int subY = textStartY + 40 + 4 + subMetrics.getAscent();
        graphics.drawString(subText, textX, subY);
    }

    private void drawMobileTexts(Graphics2D graphics, String clubName, int month, int textStartY) {
        // Mobile/Title: Pretendard Bold 18px, centered
        Font mainFont = createStyledFont(getBoldFont(), Font.BOLD, 18f, -0.01f);
        graphics.setFont(mainFont);
        graphics.setColor(new Color(33, 33, 33));
        FontMetrics mainMetrics = graphics.getFontMetrics();
        String mainText = "이달의 피드 : " + clubName + " 축하드립니다!";
        int mainX = (MOBILE_WIDTH - mainMetrics.stringWidth(mainText)) / 2;
        int mainY = textStartY + mainMetrics.getAscent();
        graphics.drawString(mainText, mainX, mainY);

        // Mobile/Sub: Pretendard Medium 12px, centered
        Font subFont = createStyledFont(getMediumFont(), Font.PLAIN, 12f, 0.01f);
        graphics.setFont(subFont);
        graphics.setColor(new Color(100, 100, 100));
        FontMetrics subMetrics = graphics.getFontMetrics();
        String subText = month + "월의 피드는 '동아리 피드'에서 확인하실 수 있습니다.";
        int subX = (MOBILE_WIDTH - subMetrics.stringWidth(subText)) / 2;
        graphics.drawString(subText, subX, mainY + 20);
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
