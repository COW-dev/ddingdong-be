package ddingdong.ddingdongBE.domain.documents.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.repository.DocumentRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Autowired
    @MockBean
    private DocumentRepository documentRepository;

    @Autowired
    @InjectMocks
    private DocumentService documentService;


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

    @DisplayName("document(자료)를 수정한다.")
    @Test
    void update() {
        //given
        Document beforeDocument = Document.builder()
                .id(1L)
                .title("beforeTitle")
                .content("beforeContent")
                .build();

        Document aferDocument = Document.builder()
                .title("afterTitle")
                .content("afterContent")
                .build();

        given(documentRepository.findById(beforeDocument.getId())).willReturn(Optional.of(beforeDocument));

        //when
        Long updatedDocumentId = documentService.update(beforeDocument.getId(), aferDocument);

        //then
        Optional<Document> foundDocument = documentRepository.findById(updatedDocumentId);
        assertThat(foundDocument.isPresent()).isTrue();
        assertThat(foundDocument.get().getTitle()).isEqualTo(aferDocument.getTitle());
        assertThat(foundDocument.get().getContent()).isEqualTo(aferDocument.getContent());
    }

}
