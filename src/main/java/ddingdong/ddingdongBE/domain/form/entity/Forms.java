package ddingdong.ddingdongBE.domain.form.entity;

import static ddingdong.ddingdongBE.domain.form.entity.FormStatus.ONGOING;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Forms {

    private final List<Form> forms;

    public Forms(List<Form> forms) {
        this.forms = forms;
    }

    public Form getActiveOrNewest() {
        Optional<Form> activeForm = findActiveForm();
        if (activeForm.isPresent()) {
            return activeForm.get();
        }
        Optional<Form> newestForm = findNewestForm();
        return newestForm.orElse(null);
    }

    public List<Form> getForms() {
        return new ArrayList<>(forms);
    }

    private Optional<Form> findNewestForm() {
        return forms.stream()
                .max(Comparator.comparing(Form::getId));
    }

    private Optional<Form> findActiveForm() {
        return forms.stream()
                .filter(form -> form.isEqualStatusTo(ONGOING))
                .findFirst();
    }
}
