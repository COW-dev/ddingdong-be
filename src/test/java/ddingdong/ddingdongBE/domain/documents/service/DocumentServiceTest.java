package ddingdong.ddingdongBE.domain.documents.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.repository.DocumentRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DocumentServiceTest extends TestContainerSupport {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;


    @DisplayName("document(자료)를 생성한다.")
    @Test
    void create() {
        //given
        Document document = Document.builder()
                .title("test")
                .build();

        //when
        Long createdDocumentId = documentService.create(document);

        //then
        Optional<Document> foundDocument = documentRepository.findById(createdDocumentId);
        assertThat(foundDocument.isPresent()).isTrue();
        assertThat(foundDocument.get().getId()).isEqualTo(createdDocumentId);
    }

}
