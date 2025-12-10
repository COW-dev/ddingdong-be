package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.BEFORE_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.END_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.RECRUITING;
import static org.assertj.core.api.Assertions.assertThat;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FacadeUserClubServiceImplTest extends TestContainerSupport {

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
        LocalDate start_recruiting_date = LocalDate.of(2025, 9, 1);
        LocalDate end_recruiting_date = LocalDate.of(2025, 12, 31);
        LocalDate recruit_available_date = LocalDate.of(2025, 8, 31);

        List<Form> forms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Club savedClub = clubRepository.save(ClubFixture.createClub());
            Form form = FormFixture.createFormWithStartAndEndDate(savedClub, start_recruiting_date,
                    end_recruiting_date);
            forms.add(form);
        }
        formRepository.saveAll(forms);

        // when
        List<UserClubListQuery> result = facadeUserClubService.findAllWithRecruitTimeCheckPoint(
                recruit_available_date);

        // then
        assertThat(result).hasSize(5);
        assertThat(result)
                .extracting(UserClubListQuery::recruitStatus)
                .allMatch(status -> status.equals(BEFORE_RECRUIT.getText()));
    }

    @DisplayName("유저: 동아리 목록 조회 - 모집 가능")
    @Test
    void findAllWithRecruitTimeCheckPoint_Recruiting() {
        // given
        LocalDate start_recruiting_date = LocalDate.of(2025, 9, 1);
        LocalDate end_recruiting_date = LocalDate.of(2025, 12, 31);
        LocalDate recruit_available_date = LocalDate.of(2025, 12, 10);

        List<Form> forms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Club savedClub = clubRepository.save(ClubFixture.createClub());
            Form form = FormFixture.createFormWithStartAndEndDate(savedClub, start_recruiting_date,
                    end_recruiting_date);
            forms.add(form);
        }
        formRepository.saveAll(forms);

        // when
        List<UserClubListQuery> result = facadeUserClubService.findAllWithRecruitTimeCheckPoint(
                recruit_available_date);

        // then
        assertThat(result).hasSize(5);
        assertThat(result)
                .extracting(UserClubListQuery::recruitStatus)
                .allMatch(status -> status.equals(RECRUITING.getText()));
    }

    @DisplayName("유저: 동아리 목록 조회 - 모집 마감")
    @Test
    void findAllWithRecruitTimeCheckPoint_EndRecruit() {
        // given
        LocalDate start_recruiting_date = LocalDate.of(2025, 9, 1);
        LocalDate end_recruiting_date = LocalDate.of(2025, 12, 1);
        LocalDate recruit_available_date = LocalDate.of(2025, 12, 10);

        List<Form> forms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Club savedClub = clubRepository.save(ClubFixture.createClub());
            Form form = FormFixture.createFormWithStartAndEndDate(savedClub, start_recruiting_date,
                    end_recruiting_date);
            forms.add(form);
        }
        formRepository.saveAll(forms);

        // when
        List<UserClubListQuery> result = facadeUserClubService.findAllWithRecruitTimeCheckPoint(
                recruit_available_date);

        // then
        assertThat(result).hasSize(5);
        assertThat(result)
                .extracting(UserClubListQuery::recruitStatus)
                .allMatch(status -> status.equals(END_RECRUIT.getText()));
    }

    @DisplayName("유저: 동아리 정보 상세 조회 - Form이 있으면 FormId를 반환한다.")
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

        assertThat(result.profileImageUrlQuery()).isNull();
        assertThat(result.introductionImageUrlQuery()).isNull();
    }

    @DisplayName("유저: 동아리 정보 상세 조회 - Form이 없으면 null을 반환한다.")
    @Test
    void getClubWithOutForm() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));

        // when
        UserClubQuery result = facadeUserClubService.getClub(savedClub.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(savedClub.getName());
        assertThat(result.category()).isEqualTo(savedClub.getCategory());
        assertThat(result.tag()).isEqualTo(savedClub.getTag());
        assertThat(result.leader()).isEqualTo(savedClub.getLeader());

        assertThat(result.formId()).isNull();

        assertThat(result.profileImageUrlQuery()).isNull();
        assertThat(result.introductionImageUrlQuery()).isNull();
    }
}
