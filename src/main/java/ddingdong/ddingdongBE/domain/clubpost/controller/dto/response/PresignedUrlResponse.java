package ddingdong.ddingdongBE.domain.clubpost.controller.dto.response;

import lombok.Builder;

@Builder
public record PresignedUrlResponse(
    String presignedUrl
) {

  public static PresignedUrlResponse of(String presignedUrl) {
    return PresignedUrlResponse.builder()
        .presignedUrl(presignedUrl)
        .build();
  }
}
