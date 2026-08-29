package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormSectionQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.UserFormQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeUserFormServiceImpl implements FacadeUserFormService {

    private final FormService formService;
    private final FormFieldService formFieldService;
    private final FormStatisticService formStatisticService;

    @Override
    public FormSectionQuery getFormSection(Long formId) {
        Form form = formService.getById(formId);
        return FormSectionQuery.from(form);
    }

    @Override
    @Cacheable(value = "formsCache", key = "'form_' + #root.args[0] + '_' + #root.args[1]")
    public UserFormQuery getUserForm(Long formId, String section) {
        Form form = formService.getById(formId);
        Club club = form.getClub();
        List<FormField> formFields = formFieldService.getAllByFormAndSection(form, section);
        return UserFormQuery.from(club, form, formFields);
    }

    @Override
    public int getApplicationCount(Long formId) {
        Form form = formService.getById(formId);
        return formStatisticService.getTotalApplicationCountByForm(form);
    }
}
