package ddingdong.ddingdongBE.domain.documents.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.repository.DocumentRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;

    @DisplayName("document(자료)를 생성한다.")
    @Test
    void create() {
        //given
        Document document = Document.builder()
                .title("test")
                .content("test")
                .build();

        //when
        Long createdDocumentId = documentService.create(document);

        //then
        Optional<Document> foundDocument = documentRepository.findById(createdDocumentId);
        assertThat(foundDocument.isPresent()).isTrue();
        assertThat(foundDocument.get().getId()).isEqualTo(createdDocumentId);
    }

}
