package ddingdong.ddingdongBE.domain.pairgame.service;

import ddingdong.ddingdongBE.common.exception.PairGameApplierException.DuplicatedPairGameApplierException;
import ddingdong.ddingdongBE.domain.pairgame.entity.PairGameApplier;
import ddingdong.ddingdongBE.domain.pairgame.repository.PairGameRepository;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameApplierAmountQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PairGameService {

    private final PairGameRepository pairGameRepository;

    @Transactional
    public PairGameApplier create(PairGameApplier pairGameApplier) {
        return pairGameRepository.save(pairGameApplier);
    }

    public PairGameApplierAmountQuery getPairGameApplierAmount() {
        int amount = (int) pairGameRepository.count();
        return PairGameApplierAmountQuery.of(amount);
    }

    public void validateStudentNumberUnique(String studentNumber) {
        if (pairGameRepository.existsByStudentNumber(studentNumber)) {
            throw new DuplicatedPairGameApplierException();
        }
    }
}
