package ddingdong.ddingdongBE.domain.fileinformation.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileTypeCategory {

    IMAGE("images/"),
    FILE("files/");

    private final String fileType;
}
