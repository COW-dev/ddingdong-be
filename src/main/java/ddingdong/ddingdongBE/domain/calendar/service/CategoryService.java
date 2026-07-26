package ddingdong.ddingdongBE.domain.calendar.service;

import ddingdong.ddingdongBE.common.exception.CalendarException;
import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.repository.CategoryRepository;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.CreateCategoryCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateCategoryCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.query.CategoryQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long create(CreateCategoryCommand command) {
        if (categoryRepository.existsByName(command.name())) {
            throw new CalendarException.DuplicatedCategoryNameException();
        }
        Category category = command.toEntity();
        try {
            Category savedCategory = categoryRepository.saveAndFlush(category);
            return savedCategory.getId();
        } catch (DataIntegrityViolationException exception) {
            throw new CalendarException.DuplicatedCategoryNameException();
        }
    }

    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Category(categoryId=" + categoryId + ")를 찾을 수 없습니다."));
    }

    public List<CategoryQuery> getAll() {
        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(CategoryQuery::from)
                .toList();
    }

    @Transactional
    public void update(Category category, UpdateCategoryCommand command) {
        if (categoryRepository.existsByNameAndIdNot(command.name(), category.getId())) {
            throw new CalendarException.DuplicatedCategoryNameException();
        }
        category.update(command);
        try {
            categoryRepository.saveAndFlush(category);
        } catch (DataIntegrityViolationException exception) {
            throw new CalendarException.DuplicatedCategoryNameException();
        }
    }

    @Transactional
    public void delete(Long categoryId) {
        Category category = getById(categoryId);
        categoryRepository.delete(category);
    }
}
