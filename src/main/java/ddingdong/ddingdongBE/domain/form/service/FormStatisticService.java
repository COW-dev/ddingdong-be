package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.MultipleFieldStatisticsQuery.OptionStatisticQuery;
import java.util.List;

public interface FormStatisticService {

    int getTotalApplicationCountByForm(Form form);

    List<DepartmentStatisticQuery> createDepartmentStatistics(int totalCount, Form form);

    List<ApplicantStatisticQuery> createApplicationStatistics(Club club, Form form);

    FieldStatisticsQuery createFieldStatisticsByForm(Form form);

    List<OptionStatisticQuery> createOptionStatistics(FormField formField);
}
