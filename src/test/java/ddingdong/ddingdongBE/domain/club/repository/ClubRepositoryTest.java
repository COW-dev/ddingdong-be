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
        clubRepository.saveAll(List.of(club, club2));
        Form form = Form.builder()
                .title("제목 1")
                .startDate(LocalDate.of(2024, 12, 13))
                .endDate(LocalDate.of(2024, 12, 20))
                .hasInterview(false)
                .sections(List.of())
                .club(club)
                .build();
        Form form2 = Form.builder()
                .title("제목 2")
                .startDate(LocalDate.of(2024, 12, 7))
                .endDate(LocalDate.of(2024, 12, 12))
                .hasInterview(false)
                .sections(List.of())
                .club(club)
                .build();
        formRepository.saveAll(List.of(form, form2));
        // when
        List<UserClubListInfo> infos = clubRepository.findAllClubListInfo(LocalDate.of(2024, 12, 30));
        // then

        assertSoftly(softly -> {
            softly.assertThat(infos.size()).isEqualTo(2);
            softly.assertThat(infos.get(0).getName()).isEqualTo("이름1");
            softly.assertThat(infos.get(1).getName()).isEqualTo("이름2");
            softly.assertThat(infos.get(0).getStart()).isEqualTo(LocalDate.of(2024, 12, 13));
            softly.assertThat(infos.get(0).getEnd()).isEqualTo(LocalDate.of(2024, 12, 20));
        });
    }
}
