package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.BEFORE_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.END_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.RECRUITING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FormFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubListQuery;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeUserClubServiceImplTest extends TestContainerSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private FacadeUserClubService facadeUserClubService;

    @DisplayName("유저: 동아리 목록 조회 - 모집 전")
    @Test
    void findAllWithRecruitTimeCheckPoint_BeforeRecruit() {
        // given
        LocalDate startRecruitingDate = LocalDate.of(2025, 9, 1);
        LocalDate endRecruitingDate = LocalDate.of(2025, 12, 31);
        LocalDate recruitAvailableDate = LocalDate.of(2025, 8, 31);

        List<Club> savedClubs = saveClubsWithSize(5);
        List<Form> forms = createFormsForClubsWithPeriod(savedClubs, startRecruitingDate,
                endRecruitingDate);
        formRepository.saveAll(forms);

        // when
        List<UserClubListQuery> result = facadeUserClubService.findAllWithRecruitTimeCheckPoint(
                recruitAvailableDate);

        // then
        assertAll(
                () -> assertThat(result).hasSize(5),
                () -> assertThat(result)
                        .extracting(UserClubListQuery::recruitStatus)
                        .allMatch(status -> status.equals(BEFORE_RECRUIT.getText()))
        );
    }

    @DisplayName("유저: 동아리 목록 조회 - 모집 가능")
    @Test
    void findAllWithRecruitTimeCheckPoint_Recruiting() {
        // given
        LocalDate startRecruitingDate = LocalDate.of(2025, 9, 1);
        LocalDate endRecruitingDate = LocalDate.of(2025, 12, 31);
        LocalDate recruitAvailableDate = LocalDate.of(2025, 12, 10);

        List<Club> savedClubs = saveClubsWithSize(5);
        List<Form> forms = createFormsForClubsWithPeriod(savedClubs, startRecruitingDate,
                endRecruitingDate);
        formRepository.saveAll(forms);

        // when
        List<UserClubListQuery> result = facadeUserClubService.findAllWithRecruitTimeCheckPoint(
                recruitAvailableDate);

        // then
        assertAll(
                () -> assertThat(result).hasSize(5),
                () -> assertThat(result)
                        .extracting(UserClubListQuery::recruitStatus)
                        .allMatch(status -> status.equals(RECRUITING.getText()))
        );
    }

    @DisplayName("유저: 동아리 목록 조회 - 모집 마감")
    @Test
    void findAllWithRecruitTimeCheckPoint_EndRecruit() {
        // given
        LocalDate startRecruitingDate = LocalDate.of(2025, 9, 1);
        LocalDate endRecruitingDate = LocalDate.of(2025, 12, 1);
        LocalDate recruitAvailableDate = LocalDate.of(2025, 12, 10);

        List<Club> savedClubs = saveClubsWithSize(5);
        List<Form> forms = createFormsForClubsWithPeriod(savedClubs, startRecruitingDate,
                endRecruitingDate);
        formRepository.saveAll(forms);

        // when
        List<UserClubListQuery> result = facadeUserClubService.findAllWithRecruitTimeCheckPoint(
                recruitAvailableDate);

        // then
        assertAll(
                () -> assertThat(result).hasSize(5),
                () -> assertThat(result)
                        .extracting(UserClubListQuery::recruitStatus)
                        .allMatch(status -> status.equals(END_RECRUIT.getText()))
        );
    }

    @DisplayName("유저: 동아리 정보 상세 조회(기본) - Form이 존재하면 Form 관련 필드를 반환한다.)")
    @Test
    void getClubWithForm() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        // when
        UserClubQuery result = facadeUserClubService.getClub(savedClub.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(savedClub.getName());
        assertThat(result.category()).isEqualTo(savedClub.getCategory());
        assertThat(result.tag()).isEqualTo(savedClub.getTag());
        assertThat(result.leader()).isEqualTo(savedClub.getLeader());

        assertThat(result.formId()).isEqualTo(savedForm.getId());
        assertThat(result.startDate()).isEqualTo(savedForm.getStartDate());
        assertThat(result.endDate()).isEqualTo(savedForm.getEndDate());
    }

    @DisplayName("유저: 동아리 정보 상세 조회 - Form이 없으면 Form 관련 필드를 Null값으로 반환한다.")
    @Test
    void getClubWithOutForm() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));

        // when
        UserClubQuery result = facadeUserClubService.getClub(savedClub.getId());

        // then
        assertThat(result.formId()).isNull();
        assertThat(result.startDate()).isNull();
        assertThat(result.endDate()).isNull();
    }

    private List<Club> saveClubsWithSize(int clubSize) {
        List<Club> savedClubs = new ArrayList<>(clubSize);
        for (int i = 0; i < clubSize; i++) {
            savedClubs.add(clubRepository.save(ClubFixture.createClub()));
        }
        return savedClubs;
    }

    private List<Form> createFormsForClubsWithPeriod(List<Club> savedClubs,
            LocalDate startRecruitingDate,
            LocalDate endRecruitingDate) {
        return savedClubs.stream()
                .map(club -> FormFixture.createFormWithStartAndEndDate(
                        club, startRecruitingDate, endRecruitingDate
                ))
                .collect(Collectors.toList());
    }
}
