package ddingdong.ddingdongBE.domain.qrstamp.service;


import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.request.StudentInfoParam;
import ddingdong.ddingdongBE.domain.qrstamp.entity.ClubStamp;
import ddingdong.ddingdongBE.domain.qrstamp.entity.StampHistory;
import ddingdong.ddingdongBE.domain.qrstamp.repository.StampHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QrStampService {

    private final StampHistoryRepository stampHistoryRepository;

    @Transactional
    public void collectStamp(StudentInfoParam param, String clubCode) {
        StampHistory stampHistory = stampHistoryRepository.findStampHistoryByStudentNameAndStudentNumber(
                        param.getStudentName(),
                        param.getStudentNumber())
                .orElse(param.toStampHistoryEntity());

        ClubStamp clubStamp = ClubStamp.getByClubCode(clubCode);
        stampHistory.collectStamp(clubStamp);

        stampHistoryRepository.save(stampHistory);
    }

}
