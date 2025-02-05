package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantRateQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentRankQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsListQuery;
import java.util.List;

public interface FormStatisticService {

    int getTotalApplicationCountByForm(Form form);

    List<DepartmentRankQuery> createDepartmentRankByForm(Form form);

    List<ApplicantRateQuery> createApplicationRateByForm(Form form);

    List<FieldStatisticsListQuery> createFieldStatisticsListByForm(Form form);
}
