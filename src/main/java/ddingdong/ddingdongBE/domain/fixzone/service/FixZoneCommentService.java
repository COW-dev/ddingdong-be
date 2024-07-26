package ddingdong.ddingdongBE.domain.fixzone.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_FIX_ZONE_COMMENT;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneCommentRequest;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FixZoneCommentService {

    private final FixZoneCommentRepository fixZoneCommentRepository;

    @Transactional
    public void create(FixZone fixZone, Club club, CreateFixZoneCommentRequest request) {
        fixZoneCommentRepository.save(request.toEntity(fixZone, club));
    }

    @Transactional
    public void update(Long clubId, Long commentId, CreateFixZoneCommentRequest request) {
        FixZoneComment fixZoneComment = getById(commentId);

        fixZoneComment.update(clubId, request.content());
    }

    @Transactional
    public void delete(Long commentId) {
        FixZoneComment fixZoneComment = getById(commentId);

        fixZoneCommentRepository.delete(fixZoneComment);
    }

    public FixZoneComment getById(Long commentId) {
        return fixZoneCommentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX_ZONE_COMMENT.getText()));
    }

}
