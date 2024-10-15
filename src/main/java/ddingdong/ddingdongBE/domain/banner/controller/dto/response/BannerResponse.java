//package ddingdong.ddingdongBE.domain.banner.controller.dto.response;
//
//import ddingdong.ddingdongBE.domain.banner.entity.Banner;
//import lombok.Builder;
//import lombok.Getter;
//
//@Getter
//public class BannerResponse {
//
//    private Long id;
//
//    private String title;
//
//    private String subTitle;
//
//    private String colorCode;
//
//    private String imgUrl;
//
//    @Builder
//    public BannerResponse(Long id, String title, String subTitle, String colorCode, String imgUrl) {
//        this.id = id;
//        this.title = title;
//        this.subTitle = subTitle;
//        this.colorCode = colorCode;
//        this.imgUrl = imgUrl;
//    }
//
//    public static BannerResponse of(Banner banner, String imgUrl) {
//        return BannerResponse.builder()
//                .id(banner.getId())
//                .title(banner.getTitle())
//                .subTitle(banner.getSubTitle())
//                .colorCode(banner.getColorCode())
//                .imgUrl(imgUrl).build();
//    }
//}
