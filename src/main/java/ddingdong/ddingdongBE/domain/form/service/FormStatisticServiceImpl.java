package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.common.utils.CalculationUtils;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.ApplicantRateQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.DepartmentRankQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormStatisticsQuery.FieldStatisticsListQuery;
import ddingdong.ddingdongBE.domain.form.service.vo.ApplicationRates;
import ddingdong.ddingdongBE.domain.form.service.vo.ApplicationRates.ApplicationRate;
import ddingdong.ddingdongBE.domain.form.service.vo.HalfYear;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormAnswerRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.DepartmentInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormStatisticServiceImpl implements FormStatisticService {

    private static final int APPLICATION_COMPARE_COUNT = 5;
    private static final int NOT_EXIST_APPLICATION = 0;
    private static final int DEFAULT_APPLICATION_RATE = 100;

    private final FormApplicationRepository formApplicationRepository;
    private final FormFieldRepository formFieldRepository;
    private final FormAnswerRepository formAnswerRepository;

    @Override
    public int getTotalApplicationCountByForm(Form form) {
        return formApplicationRepository.countByForm(form).intValue();
    }

    @Override
    public List<DepartmentRankQuery> createDepartmentRankByForm(Form form) {
        List<DepartmentInfo> departmentInfos = formApplicationRepository.findTopFiveDepartmentsByForm(form.getId());
        int totalCount = getTotalApplicationCountByForm(form);

        return IntStream.range(0, departmentInfos.size())
                .mapToObj(index -> {
                    DepartmentInfo departmentInfo = departmentInfos.get(index);
                    int rank = index + 1;
                    String department = departmentInfo.getDepartment();
                    int count = parseToInt(departmentInfo.getCount());
                    int rate = CalculationUtils.calculateRate(count, totalCount);
                    return new DepartmentRankQuery(rank, department, count, rate);
                })
                .toList();
    }

    @Override
    public List<ApplicantRateQuery> createApplicationRateByForm(Form form) {
        List<ApplicantRateQuery> results = new ArrayList<>();

        HalfYear halfYear = HalfYear.from(form.getEndDate());
        ApplicationRates applicationRates = new ApplicationRates();
        for (int count = 0; count < APPLICATION_COMPARE_COUNT; count++) {
            int maxCount = parseToInt(formApplicationRepository.findMaxApplicationCountByDateRange(
                    halfYear.getHalfStartDate(),
                    halfYear.getHalfEndDate()
            ));
            if (maxCount == NOT_EXIST_APPLICATION) {
                break;
            }
            int rate = (applicationRates.getPrevious() == null) ? DEFAULT_APPLICATION_RATE :
                    CalculationUtils.calculateRate(maxCount, applicationRates.getPrevious().getCount());
            applicationRates.add(halfYear.getLabel(), maxCount, rate);
            halfYear.minusHalves();
        }
        return applicationRates.getApplicationRates().stream()
                .map(ApplicationRate::toQuery)
                .toList();
    }

    @Override
    public List<FieldStatisticsListQuery> createFieldStatisticsListByForm(Form form) {
        List<FormField> formFields = formFieldRepository.findAllByForm(form);
        return formFields.stream()
                .map(this::buildFieldStatisticsList)
                .toList();
    }

    private FieldStatisticsListQuery buildFieldStatisticsList(FormField formField) {
        int answerCount = formAnswerRepository.countByFormField(formField);
        return new FieldStatisticsListQuery(formField.getId(), formField.getQuestion(), answerCount,
                formField.getFieldType());
    }

    private int parseToInt(Integer count) {
        return count == null ? 0 : count;
    }
}
