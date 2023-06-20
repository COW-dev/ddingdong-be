package ddingdong.ddingdongBE.domain.imageinformation.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageCategory {

    CLUB("club-image/"),
    NOTICE("notice-image/");

    private final String filePath;

}
