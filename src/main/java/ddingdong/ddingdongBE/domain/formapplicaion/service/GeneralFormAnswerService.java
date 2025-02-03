package ddingdong.ddingdongBE.domain.formapplicaion.service;

import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplicaion.repository.FormAnswerRepository;
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

}
