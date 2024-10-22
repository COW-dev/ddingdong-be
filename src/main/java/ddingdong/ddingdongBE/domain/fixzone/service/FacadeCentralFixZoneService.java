package ddingdong.ddingdongBE.domain.fixzone.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_FIX;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.FIX_ZONE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetDetailFixZoneResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetFixZoneCommentResponse;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralMyFixZoneListQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeCentralFixZoneService {

    private final FixZoneService fixZoneService;
    private final S3FileService s3FileService;

    @Transactional
    public Long create(Club club, CreateFixZoneRequest request) {
        FixZone createdFixZone = request.toEntity(club);
        return fixZoneService.save(createdFixZone);
    }

    public List<CentralMyFixZoneListQuery> getMyFixZones(Long clubId) {
        return fixZoneService.findAllByClubId(clubId)
                .stream()
                .map(CentralMyFixZoneListQuery::from)
                .toList();
    }

    public GetDetailFixZoneResponse getFixZoneDetail(Long fixZoneId) {
        FixZone fixZone = fixZoneService.getById(fixZoneId);
        return GetDetailFixZoneResponse.of(
                fixZone.getId(),
                fixZone.getClub().getLocation().getValue(),
                fixZone.getClub().getName(),
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
                    return GetFixZoneCommentResponse.of(comment, profileImageUrl);
                })
                .toList();
    }

    @Transactional
    public void update(Long id, UpdateFixZoneRequest request) {
        FixZone fixZone = getById(id);

        fixZone.update(
                request.getTitle(),
                request.getContent()
        );
    }

    @Transactional
    public void delete(Long id) {
        FixZone fixZone = getById(id);
        fixZoneRepository.delete(fixZone);
    }

    public FixZone getById(Long id) {
        return fixZoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX.getText()));
    }

}
