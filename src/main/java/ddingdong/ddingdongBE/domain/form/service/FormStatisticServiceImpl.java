package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.common.utils.CalculationUtils;
import ddingdong.ddingdongBE.common.utils.TimeUtils;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import ddingdong.ddingdongBE.domain.form.repository.dto.FieldListInfo;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentStatisticQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsQuery.FieldStatisticsListQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.MultipleFieldStatisticsQuery.OptionStatisticQuery;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormAnswerRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.DepartmentInfo;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.RecentFormInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormStatisticServiceImpl implements FormStatisticService {

    private static final int DEPARTMENT_INFORMATION_SIZE = 5;
    private static final int APPLICANT_RATIO_INFORMATION_SIZE = 3;
    private static final int DEFAULT_APPLICATION_RATE = 100;

    private final FormApplicationRepository formApplicationRepository;
    private final FormFieldRepository formFieldRepository;
    private final FormAnswerRepository formAnswerRepository;

    @Override
    public int getTotalApplicationCountByForm(Form form) {
        return formApplicationRepository.countByForm(form).intValue();
    }

    @Override
    public List<DepartmentStatisticQuery> createDepartmentStatistics(int totalCount, Form form) {
        List<DepartmentInfo> departmentInfos = formApplicationRepository.findTopDepartmentsByFormId(
                form.getId(),
                DEPARTMENT_INFORMATION_SIZE
        );

        return IntStream.range(0, departmentInfos.size())
                .mapToObj(index -> {
                    DepartmentInfo departmentInfo = departmentInfos.get(index);
                    int rank = index + 1;
                    String department = departmentInfo.getDepartment();
                    int count = parseToInt(departmentInfo.getCount());
                    int ratio = CalculationUtils.calculateRatio(count, totalCount);
                    return new DepartmentStatisticQuery(rank, department, count, ratio);
                })
                .toList();
    }

    @Override
    public List<ApplicantStatisticQuery> createApplicationStatistics(Club club, Form form) {
        LocalDate endDate = form.getEndDate();
        List<RecentFormInfo> recentForms = formApplicationRepository.findRecentFormByDateWithApplicationCount(
                club.getId(),
                endDate,
                APPLICANT_RATIO_INFORMATION_SIZE
        );

        return IntStream.range(0, recentForms.size())
                .mapToObj(index -> {
                    RecentFormInfo recentFormInfo = recentForms.get(index);

                    String label = TimeUtils.getYearAndMonth(recentFormInfo.getDate());
                    int count = parseToInt(recentFormInfo.getCount());
                    if (index == 0) {
                        return new ApplicantStatisticQuery(label, count, 0, 0);
                    }
                    int beforeCount = parseToInt(recentForms.get(index - 1).getCount());
                    int compareRatio = CalculationUtils.calculateDifferenceRatio(beforeCount, count);
                    int compareValue = CalculationUtils.calculateDifference(beforeCount, count);

                    return new ApplicantStatisticQuery(label, count, compareRatio, compareValue);
                })
                .toList();
    }

    @Override
    public FieldStatisticsQuery createFieldStatisticsByForm(Form form) {
        List<String> sections = form.getSections();
        List<FieldListInfo> fieldListInfos = formFieldRepository.findFieldWithAnswerCountByFormId(form.getId());
        List<FieldStatisticsListQuery> fieldStatisticsListQueries = toFieldListQueries(fieldListInfos);
        return new FieldStatisticsQuery(sections, fieldStatisticsListQueries);
    }

    @Override
    public List<OptionStatisticQuery> createOptionStatistics(FormField formField) {
        List<String> options = formField.getOptions();
        int answerCount = formAnswerRepository.countByFormField(formField);
        return options.stream().map(option -> {
                    int count = parseToInt(formAnswerRepository.countAnswerByOption(option));
                    int ratio = CalculationUtils.calculateRatio(count, answerCount);
                    return new OptionStatisticQuery(option, count, ratio);
                })
                .toList();
    }

    private List<FieldStatisticsListQuery> toFieldListQueries(List<FieldListInfo> fieldListInfos) {
        return fieldListInfos.stream()
                .map(FieldStatisticsListQuery::from)
                .toList();
    }

    private int parseToInt(Integer count) {
        return count == null ? 0 : count;
    }
}
