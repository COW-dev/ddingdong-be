package ddingdong.ddingdongBE.domain.qrstamp.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.INVALID_STAMP_COUNT_FOR_APPLY;
import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_QR_STAMP_HISTORY;

import ddingdong.ddingdongBE.domain.event.controller.dto.request.ApplyEventRequest;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.request.CollectStampRequest;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.AppliedUsersResponse;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.CollectedStampsResponse;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.CollectionResultResponse;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.DetailAppliedUserResponse;
import ddingdong.ddingdongBE.domain.qrstamp.entity.ClubStamp;
import ddingdong.ddingdongBE.domain.qrstamp.entity.StampHistory;
import ddingdong.ddingdongBE.domain.qrstamp.repository.StampHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QrStampService {

    private final StampHistoryRepository stampHistoryRepository;

    @Transactional
    public String collectStamp(CollectStampRequest request, LocalDateTime collectedAt) {
        StampHistory stampHistory = stampHistoryRepository.findStampHistoryByStudentNameAndStudentNumber(
                        request.getStudentName(),
                        request.getStudentNumber())
                .orElse(request.toStampHistoryEntity());
        if (stampHistory.isCompleted()) {
            return "10개의 벚꽃을 모두 채우셨습니다. 이벤트 응모를 완료해주세요!";
        }
        ClubStamp clubStamp = ClubStamp.getByClubCode(request.getClubCode());
        stampHistory.collectStamp(clubStamp, collectedAt);

        stampHistoryRepository.save(stampHistory);
        return "ok";
    }

    public CollectionResultResponse getCollectionResult(String studentName, String studentNumber) {
        StampHistory stampHistory = stampHistoryRepository.findStampHistoryByStudentNameAndStudentNumber(
                        studentName, studentNumber)
                .orElse(StampHistory.builder()
                        .studentNumber(studentNumber)
                        .studentName(studentName).build());

        List<CollectedStampsResponse> collectedStampsResponse = stampHistory.getCollectedStamps().keySet().stream()
                .map(stamp -> CollectedStampsResponse.of(stamp.getName(),
                        stampHistory.getCollectedStamps().get(stamp)))
                .toList();
        return CollectionResultResponse.of(stampHistory.isCompleted(), collectedStampsResponse);
    }

    @Transactional
    public void applyEvent(ApplyEventRequest request, List<String> imageUrls) {
        StampHistory stampHistory = stampHistoryRepository.findStampHistoryByStudentNumber(
            request.studentNumber()
            )
            .orElseThrow(() -> new NoSuchElementException(NO_SUCH_QR_STAMP_HISTORY.getText()));

        validateEventIsCompleted(stampHistory);

        stampHistory.apply(request.telephone(), imageUrls.get(0));
    }

    public Long findByStudentNumber(String studentNumber) {
        StampHistory stampHistory = stampHistoryRepository.findStampHistoryByStudentNumber(studentNumber)
            .orElseThrow(() -> new NoSuchElementException(NO_SUCH_QR_STAMP_HISTORY.getText()));

        return stampHistory.getId();
    }

    public List<AppliedUsersResponse> findAllAppliedUsers() {
        List<StampHistory> appliedStampHistories = stampHistoryRepository.findAllByCertificationImageUrlIsNotNull();
        return appliedStampHistories.stream()
                .map(AppliedUsersResponse::from)
                .collect(Collectors.toList());
    }

    public DetailAppliedUserResponse findAppliedUser(Long stampHistoryId) {
        StampHistory stampHistory = stampHistoryRepository.findById(stampHistoryId)
                .orElseThrow(() -> new NoSuchElementException("응모 내역이 존재하지 않습니다."));

        return DetailAppliedUserResponse.from(stampHistory);

    }

    private void validateEventIsCompleted(StampHistory stampHistory) {
        if (!stampHistory.isCompleted()) {
            throw new IllegalArgumentException(INVALID_STAMP_COUNT_FOR_APPLY.getText());
        }
    }

}
