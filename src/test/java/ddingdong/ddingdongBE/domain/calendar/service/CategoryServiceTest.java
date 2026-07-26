package ddingdong.ddingdongBE.domain.calendar.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import ddingdong.ddingdongBE.common.exception.CalendarException;
import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.repository.CategoryRepository;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.CreateCategoryCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @DisplayName("카테고리 생성 중 DB unique 제약 위반을 도메인 중복 예외로 변환한다")
    @Test
    void createCategoryConvertsUniqueConstraintViolation() {
        // given
        CreateCategoryCommand command = new CreateCategoryCommand("중복 카테고리", "#FFFFFF");
        given(categoryRepository.existsByName(command.name())).willReturn(false);
        given(categoryRepository.saveAndFlush(any(Category.class)))
                .willThrow(new DataIntegrityViolationException("uk_category_name"));

        // when // then
        assertThatThrownBy(() -> categoryService.create(command))
                .isInstanceOf(CalendarException.DuplicatedCategoryNameException.class);
    }
}
