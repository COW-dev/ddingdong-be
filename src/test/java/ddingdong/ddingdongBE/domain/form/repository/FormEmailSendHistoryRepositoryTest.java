package ddingdong.ddingdongBE.domain.form.repository;

import static ddingdong.ddingdongBE.common.fixture.ClubFixture.createClub;
import static ddingdong.ddingdongBE.common.fixture.FormEmailSendHistoryFixture.createFormEmailSendHistory;
import static ddingdong.ddingdongBE.common.fixture.FormFixture.createForm;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormEmailSendHistoryRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private FormEmailSendHistoryRepository formEmailSendHistoryRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private ClubRepository clubRepository;

    @DisplayName("formId와 statuses에 해당하는 각 status별 가장 최신 FormEmailSendHistory를 조회한다")
    @Test
    void findLatestByFormIdAndStatuses() {
        // given
        Club club = clubRepository.save(createClub());
        Form form = formRepository.save(createForm(club));

        formEmailSendHistoryRepository.save(
                createFormEmailSendHistory(form, FormApplicationStatus.FIRST_PASS));
        FormEmailSendHistory firstPassLatest = formEmailSendHistoryRepository.save(
                createFormEmailSendHistory(form, FormApplicationStatus.FIRST_PASS));

        formEmailSendHistoryRepository.save(
                createFormEmailSendHistory(form, FormApplicationStatus.FINAL_PASS));
        FormEmailSendHistory finalPassLatest = formEmailSendHistoryRepository.save(
                createFormEmailSendHistory(form, FormApplicationStatus.FINAL_PASS));

        FormEmailSendHistory firstFailLatest = formEmailSendHistoryRepository.save(
                createFormEmailSendHistory(form, FormApplicationStatus.FIRST_FAIL));

        List<FormApplicationStatus> statuses = List.of(
                FormApplicationStatus.FIRST_PASS,
                FormApplicationStatus.FINAL_PASS,
                FormApplicationStatus.FIRST_FAIL
        );

        // when
        List<FormEmailSendHistory> result = formEmailSendHistoryRepository.findLatestByFormIdAndStatuses(
                form.getId(), statuses);

        // then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(FormEmailSendHistory::getId)
                .containsExactlyInAnyOrder(
                        firstPassLatest.getId(),
                        finalPassLatest.getId(),
                        firstFailLatest.getId()
                );
    }

    @DisplayName("조회 대상 statuses에 포함되지 않은 status의 history는 조회되지 않는다")
    @Test
    void findLatestByFormIdAndStatuses_excludesUnrequestedStatuses() {
        // given
        Club club = clubRepository.save(createClub());
        Form form = formRepository.save(createForm(club));

        FormEmailSendHistory firstPass = formEmailSendHistoryRepository.save(
                createFormEmailSendHistory(form, FormApplicationStatus.FIRST_PASS));
        formEmailSendHistoryRepository.save(
                createFormEmailSendHistory(form, FormApplicationStatus.FINAL_FAIL));

        List<FormApplicationStatus> statuses = List.of(FormApplicationStatus.FIRST_PASS);

        // when
        List<FormEmailSendHistory> result = formEmailSendHistoryRepository.findLatestByFormIdAndStatuses(
                form.getId(), statuses);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(firstPass.getId());
    }

    @DisplayName("다른 formId의 history는 조회되지 않는다")
    @Test
    void findLatestByFormIdAndStatuses_excludesOtherFormHistories() {
        // given
        Club club = clubRepository.save(createClub());
        Form form1 = formRepository.save(createForm(club));
        Form form2 = formRepository.save(createForm(club));

        FormEmailSendHistory form1History = formEmailSendHistoryRepository.save(
                createFormEmailSendHistory(form1, FormApplicationStatus.FIRST_PASS));
        formEmailSendHistoryRepository.save(
                createFormEmailSendHistory(form2, FormApplicationStatus.FIRST_PASS));

        List<FormApplicationStatus> statuses = List.of(FormApplicationStatus.FIRST_PASS);

        // when
        List<FormEmailSendHistory> result = formEmailSendHistoryRepository.findLatestByFormIdAndStatuses(
                form1.getId(), statuses);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(form1History.getId());
    }

    @DisplayName("해당하는 history가 없으면 빈 리스트를 반환한다")
    @Test
    void findLatestByFormIdAndStatuses_returnsEmptyWhenNoMatch() {
        // given
        Club club = clubRepository.save(createClub());
        Form form = formRepository.save(createForm(club));

        List<FormApplicationStatus> statuses = List.of(FormApplicationStatus.FIRST_PASS);

        // when
        List<FormEmailSendHistory> result = formEmailSendHistoryRepository.findLatestByFormIdAndStatuses(
                form.getId(), statuses);

        // then
        assertThat(result).isEmpty();
    }
}