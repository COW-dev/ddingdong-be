package ddingdong.ddingdongBE.domain.fixzone.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_FIX;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.FIX_ZONE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetDetailFixZoneResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetFixZoneCommentResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetFixZoneResponse;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FixZoneService {

    private final FixZoneRepository fixZoneRepository;
    private final FileInformationService fileInformationService;

    @Transactional
    public Long create(Club club, CreateFixZoneRequest request) {
        FixZone createdFixZone = request.toEntity(club);

        return fixZoneRepository.save(createdFixZone).getId();
    }

    public GetDetailFixZoneResponse getFixZoneDetail(Long fixZoneId) {
        FixZone fixZone = fixZoneRepository.findById(fixZoneId)
            .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX.getText()));

        List<String> imageUrls = fileInformationService.getImageUrls(
            IMAGE.getFileType() + FIX_ZONE.getFileDomain() + fixZone.getId()
        );

        return GetDetailFixZoneResponse.from(
            fixZone.getId(),
            fixZone.getTitle(),
            fixZone.getCreatedAt(),
            fixZone.getContent(),
            fixZone.isCompleted(),
            imageUrls,
            getCommentResponses(fixZone)
        );
    }

    private List<GetFixZoneCommentResponse> getCommentResponses(FixZone fixZone) {
        List<FixZoneComment> comments = fixZone.getFixZoneComments();

        return comments.stream()
            .map(comment -> {
                List<String> profileImageUrls = fileInformationService.getImageUrls(
                    IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + comment.getClub().getId()
                );
                String profileImageUrl = profileImageUrls.isEmpty() ? null : profileImageUrls.get(0);
                return GetFixZoneCommentResponse.from(comment, profileImageUrl);
            })
            .toList();
    }

    public List<GetFixZoneResponse> getAll() {
        return fixZoneRepository.findAll().stream()
            .map(GetFixZoneResponse::from)
            .toList();
    }

    @Transactional
    public void update(Long fixZoneId, UpdateFixZoneRequest request) {
        FixZone fixZone = getById(fixZoneId);

        fixZone.update(
            request.getTitle(),
            request.getContent()
        );
    }

    @Transactional
    public void updateToComplete(Long fixZoneId) {
        FixZone fixZone = getById(fixZoneId);

        fixZone.updateToComplete();
    }

    @Transactional
    public void delete(Long fixZoneId) {
        FixZone fixZone = getById(fixZoneId);
        fixZoneRepository.delete(fixZone);
    }

    public List<GetFixZoneResponse> getMyFixZones(Long clubId) {
        return fixZoneRepository.findAllByClubId(clubId)
            .stream()
            .map(GetFixZoneResponse::from)
            .toList();
    }

    public FixZone getById(Long fixZoneId) {
        return fixZoneRepository.findById(fixZoneId)
            .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX.getText()));
    }

}
