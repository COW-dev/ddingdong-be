package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.auth.service.AuthService;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.dto.command.CreateClubCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.AdminClubListQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.S3FileService;
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
    private final S3FileService s3FileService;


    @Override
    @Transactional
    public Long create(CreateClubCommand command) {
        User clubUser = authService.registerClubUser(command.authId(), command.password(), command.clubName());

        Club club = command.toEntity(clubUser);
        return clubService.save(club);
    }

    @Override
    public List<AdminClubListQuery> findAll() {
        return clubService.findAll().stream()
                .map(club -> AdminClubListQuery.of(club, s3FileService.getUploadedFileUrl(club.getProfileImageKey())))
                .toList();
    }

    @Override
    @Transactional
    public void deleteClub(Long clubId) {
        clubService.delete(clubId);
    }
}
