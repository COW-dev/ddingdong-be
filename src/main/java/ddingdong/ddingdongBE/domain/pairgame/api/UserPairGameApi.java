package ddingdong.ddingdongBE.domain.pairgame.api;

import ddingdong.ddingdongBE.domain.pairgame.controller.dto.request.CreatePairGameApplierRequest;
import ddingdong.ddingdongBE.domain.pairgame.controller.dto.response.PairGameApplierAmountResponse;
import ddingdong.ddingdongBE.domain.pairgame.controller.dto.response.PairGameMetaDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "PairGame - User", description = "User PairGame API")
@RequestMapping("/server")
public interface UserPairGameApi {

    @Operation(summary = "응모자 생성 API")
    @ApiResponse(responseCode = "201", description = "응모자 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/pair-game/appliers")
    void createPairGameApplier(
            @Valid @RequestPart("request") CreatePairGameApplierRequest request,
            @RequestPart("file") MultipartFile file
    );

    @Operation(summary = "응모자 현황 조회 API")
    @ApiResponse(responseCode = "200", description = "응모자 현황 조회 성공")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pair-game/appliers/amount")
    PairGameApplierAmountResponse getPairGameApplierAmount();

    @Operation(summary = "게임 메타데이터 조회 API")
    @ApiResponse(responseCode = "200", description = "게임 메타데이터 조회 성공")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/pair-game/metadata")
    PairGameMetaDataResponse getPairGameMetaData();
}
