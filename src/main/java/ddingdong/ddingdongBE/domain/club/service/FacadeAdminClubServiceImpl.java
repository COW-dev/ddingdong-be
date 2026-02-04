package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.auth.service.AuthService;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.dto.command.CreateClubCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.AdminClubListQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.fixzone.service.FixZoneService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeAdminClubServiceImpl implements FacadeAdminClubService {

    private final ClubService clubService;
    private final AuthService authService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;
    private final FixZoneService fixZoneService;


    @Override
    @Transactional
    public Long create(CreateClubCommand command) {
        User clubUser = authService.registerClubUser(command.authId(), command.password(), command.clubName());

        Club club = command.toEntity(clubUser);
        return clubService.save(club);
    }

    @Override
    public List<AdminClubListQuery> findAll() {
        return clubService.getAll().stream()
                .map(club -> {
                    UploadedFileUrlAndNameQuery clubProfileImageQuery = fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(
                                    DomainType.CLUB_PROFILE, club.getId())
                            .stream()
                            .map(fileMetaData -> s3FileService.getUploadedFileUrlAndName(fileMetaData.getFileKey(), fileMetaData.getFileName()))
                            .findFirst()
                            .orElse(null);
                    return AdminClubListQuery.of(club, clubProfileImageQuery);
                })
                .toList();
    }

    @Override
    @Transactional
    public void deleteClub(Long clubId) {
        fixZoneService.deleteAllByClubId(clubId);
        clubService.delete(clubId);
    }
}
