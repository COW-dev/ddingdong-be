package ddingdong.ddingdongBE.domain.club.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.dto.UserClubListInfo;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ClubRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FormRepository formRepository;


    @DisplayName("클럽 목록 전체조회에 필요한 모든 클럽 및 폼지 정보를 조회한다. 입력 날짜와 가장 가까운 폼지를 조회한다.")
    @Test
    void test() {
        // given
        Club club = Club.builder()
                .user(null)
                .name("이름1")
                .build();
        Club club2 = Club.builder()
                .user(null)
                .name("이름2")
                .build();
        Club savedClub = clubRepository.save(club);
        Club savedClub2 = clubRepository.save(club2);
        Form clubForm = Form.builder()
                .title("클럽1 최신 폼지")
                .startDate(LocalDate.of(2024, 12, 13))
                .endDate(LocalDate.of(2024, 12, 20))
                .hasInterview(false)
                .sections(List.of())
                .club(savedClub)
                .build();
        Form clubForm2 = Form.builder()
                .title("클럽1 올드 폼지")
                .startDate(LocalDate.of(2024, 11, 13))
                .endDate(LocalDate.of(2024, 11, 20))
                .hasInterview(false)
                .sections(List.of())
                .club(savedClub)
                .build();
        Form club2Form = Form.builder()
                .title("클럽2 최신 폼지")
                .startDate(LocalDate.of(2024, 12, 7))
                .endDate(LocalDate.of(2024, 12, 12))
                .hasInterview(false)
                .sections(List.of())
                .club(savedClub2)
                .build();
        formRepository.saveAll(List.of(clubForm, clubForm2, club2Form));
        // when
        List<UserClubListInfo> infos = clubRepository.findAllClubListInfo(LocalDate.of(2024, 12, 30));
        // then

        assertSoftly(softly -> {
            softly.assertThat(infos.size()).isEqualTo(2);
            softly.assertThat(infos.get(0).getName()).isEqualTo("클럽1 최신 폼지");
            softly.assertThat(infos.get(1).getName()).isEqualTo("클럽2 최신 폼지");
        });
    }
}
