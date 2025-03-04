package ddingdong.ddingdongBE.domain.activityreport.entity;

import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.activityreport.repository.ActivityReportRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ActivityReportTest extends DataJpaTestSupport {

  @Autowired
  private ActivityReportRepository activityReportRepository;

  @Autowired
  private EntityManager entityManager;

  @DisplayName("활동보고서 삭제 시 soft delete 적용한다.")
  @Test
  void soft_delete() {
    // given
    ActivityReport activityReport = ActivityReport.builder()
        .content("내용입니다.")
        .build();
    activityReportRepository.save(activityReport);
    // when
    activityReportRepository.delete(activityReport);
    entityManager.flush();
    // then
    ActivityReport findActivityReport = (ActivityReport) entityManager.createNativeQuery(
        "select * from activity_report where content = :content limit 1", ActivityReport.class)
        .setParameter("content", activityReport.getContent())
        .getSingleResult();
    Assertions.assertThat(findActivityReport).isNotNull();
    Assertions.assertThat(findActivityReport.getDeletedAt()).isNotNull();
  }
}
