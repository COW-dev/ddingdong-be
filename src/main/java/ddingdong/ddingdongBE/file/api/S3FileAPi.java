package ddingdong.ddingdongBE.file.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.common.exception.ErrorResponse;
import ddingdong.ddingdongBE.file.controller.dto.response.UploadUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "S3File", description = "AWS S3 File API")
@RequestMapping("/server/file")
public interface S3FileAPi {

    @Operation(summary = "AWS S3 presignedUrl 발급 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "presignedUrl 발급 성공"),
            @ApiResponse(responseCode = "400",
                    description = "AWS 오류(서버 오류)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "AWS 서비스 오류(서버 오류)",
                                            value = """
                                                    {
                                                      "status": 500,
                                                      "message": "AWS 서비스 오류로 인해 Presigned URL 생성에 실패했습니다.",
                                                      "timestamp": "2024-08-22T00:08:46.990585"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(name = "AWS 클라이언트 오류(서버 오류)",
                                            value = """
                                                    {
                                                      "status": 500,
                                                      "message": "AWS 클라이언트 오류로 인해 Presigned URL 생성에 실패했습니다.",
                                                      "timestamp": "2024-08-22T00:08:46.990585"
                                                    }
                                                    """
                                    )
                            })
            )
    })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/upload-url")
    UploadUrlResponse getPreSignedUrl(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @RequestParam("fileName") String fileName);

}
