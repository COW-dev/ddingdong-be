package ddingdong.ddingdongBE.domain.form.repository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private FormRepository formRepository;
    @Autowired
    private ClubRepository clubRepository;

    private static final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("동아리에 주어진 기간과 겹친 폼지를 반환한다.")
    @Test
    void shouldFindOverlappingForms() {
        // given
        Club testClub = clubRepository.save(
                fixture.giveMeBuilder(Club.class)
                        .set("deletedAt", null)
                        .set("user", null)
                        .set("clubMembers", null)
                        .set("score", null)
                        .sample()
        );

        Form form1 = fixture.giveMeBuilder(Form.class)
                .set("title", "제목입니다")
                .set("club", testClub)
                .set("startDate", LocalDate.of(2025, 7, 1))
                .set("endDate", LocalDate.of(2025, 7, 10))
                .sample();
        formRepository.save(form1);

        // when
        List<Form> overlappingForms = formRepository.findOverlappingForms(
                testClub.getId(),
                LocalDate.of(2025, 7, 8),
                LocalDate.of(2025, 7, 11)
        );

        // then
        Assertions.assertThat(overlappingForms.size()).isEqualTo(1);
        Assertions.assertThat(overlappingForms.get(0).getTitle()).isEqualTo("제목입니다");
    }
}
