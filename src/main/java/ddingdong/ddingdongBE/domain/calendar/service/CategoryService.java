package ddingdong.ddingdongBE.domain.calendar.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long save(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return savedCategory.getId();
    }

    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Category(categoryId=" + categoryId + ")를 찾을 수 없습니다."));
    }

    public Category getByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFound("Category(name=" + name + ")를 찾을 수 없습니다."));
    }
}
