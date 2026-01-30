package ddingdong.ddingdongBE.domain.pairgame.controller;

import ddingdong.ddingdongBE.domain.pairgame.api.UserPairGameApi;
import ddingdong.ddingdongBE.domain.pairgame.controller.dto.request.CreatePairGameApplierRequest;
import ddingdong.ddingdongBE.domain.pairgame.controller.dto.response.PairGameApplierAmountResponse;
import ddingdong.ddingdongBE.domain.pairgame.controller.dto.response.PairGameMetaDataResponse;
import ddingdong.ddingdongBE.domain.pairgame.service.FacadeUserPairGameService;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameApplierAmountQuery;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameMetaDataQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserPairGameController implements UserPairGameApi {

    private final FacadeUserPairGameService facadeUserPairGameService;

    @Override
    public void createPairGameApplier(CreatePairGameApplierRequest createPairGameApplierRequest, MultipartFile studentFeeImageFile) {
        facadeUserPairGameService.createApplier(createPairGameApplierRequest.toCommand(), studentFeeImageFile);
    }

    @Override
    public PairGameApplierAmountResponse getPairGameApplierAmount() {
        PairGameApplierAmountQuery query = facadeUserPairGameService.getPairGameApplierAmount();
        return PairGameApplierAmountResponse.from(query);
    }

    @Override
    public PairGameMetaDataResponse getPairGameMetaData() {
        PairGameMetaDataQuery query = facadeUserPairGameService.getPairGameMetaData();
        return PairGameMetaDataResponse.from(query);
    }
}
