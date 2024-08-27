package ddingdong.ddingdongBE.file.api;

import ddingdong.ddingdongBE.file.controller.dto.response.UploadUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "S3File", description = "AWS S3 File API")
@RequestMapping("/server/file")
public interface S3FileAPi {

    @Operation(summary = "AWS S3 presignedUrl 발급 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "presignedUrl 발급 성공")})
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/upload-url")
    UploadUrlResponse getUploadUrl(@RequestParam("fileName") String fileName);

}
