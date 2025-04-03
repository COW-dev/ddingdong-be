package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFormAnswerService implements FormAnswerService {

    private final FormAnswerRepository formAnswerRepository;

    @Transactional
    @Override
    public void createAll(List<FormAnswer> formAnswers) {
        formAnswerRepository.saveAll(formAnswers);
    }

    @Override
    public List<FormAnswer> getAllByApplication(FormApplication formApplication) {
        return formAnswerRepository.findAllByFormApplication(formApplication);
    }

    @Override
    public List<FileMetaData> getAllFileByForm(Form form) {
        return formAnswerRepository.getAllFileByForm(form.getId());
    }
}
