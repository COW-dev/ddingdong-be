package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FixZoneCommentServiceImpl implements FixZoneCommentService {

    private final FixZoneCommentRepository fixZoneCommentRepository;

    @Override
    @Transactional
    public Long save(FixZoneComment fixZoneComment) {
        FixZoneComment savedFixZoneComment = fixZoneCommentRepository.save(fixZoneComment);
        return savedFixZoneComment.getId();
    }

    @Override
    public FixZoneComment getById(Long fixZoneCommentId) {
        return fixZoneCommentRepository.findById(fixZoneCommentId)
                .orElseThrow(
                        () -> new ResourceNotFound("FixZoneComment(commentId=" + fixZoneCommentId + ")를 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public void delete(Long fixZoneCommentId) {
        FixZoneComment fixZoneComment = getById(fixZoneCommentId);
        fixZoneCommentRepository.delete(fixZoneComment);
    }
}
