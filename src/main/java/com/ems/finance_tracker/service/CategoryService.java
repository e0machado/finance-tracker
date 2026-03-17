package com.ems.finance_tracker.service;

import com.ems.finance_tracker.dto.CategoryDTO;
import com.ems.finance_tracker.exception.BusinessException;
import com.ems.finance_tracker.exception.ResourceNotFoundException;
import com.ems.finance_tracker.model.entity.Category;
import com.ems.finance_tracker.model.mapper.CategoryMapper;
import com.ems.finance_tracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for managing {@link Category} business operations.
 * Handles validation, persistence coordination and DTO/entity transformations.
 *
 * @author Evandro Machado
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Retrieves all categories from the system.
     *
     * @return a list of {@link CategoryDTO.Response} representing all registered categories
     */
    public List<CategoryDTO.Response> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a single category by its identifier.
     *
     * @param id the category identifier
     * @return a {@link CategoryDTO.Response} with the category data
     * @throws ResourceNotFoundException if the category does not exist
     */
    public CategoryDTO.Response findCategoryById(Long id) {
        Category category = findEntityById(id);

        return categoryMapper.toResponse(category);
    }

    /**
     * Creates and persists a new category.
     * Applies business rules such as name uniqueness validation.
     *
     * @param dto the category creation request data
     * @return a {@link CategoryDTO.Response} representing the persisted category
     * @throws BusinessException if the category name is already in use
     */
    @Transactional
    public CategoryDTO.Response saveCategory(CategoryDTO.Request dto) {
        validateNameUniqueness(dto.name(), null);

        Category category = categoryMapper.toEntity(dto);

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    /**
     * Updates an existing category's information.
     *
     * @param id the identifier of the category to be updated
     * @param dto the DTO containing updated category data
     * @return a {@link CategoryDTO.Response} representing the updated category
     * @throws ResourceNotFoundException if the category does not exist
     * @throws BusinessException if the category name is already in use by another category
     */
    @Transactional
    public CategoryDTO.Response updateCategory(Long id, CategoryDTO.Update dto) {
        if (dto.name().isPresent()) {
            validateNameUniqueness(dto.name().get(), id);
        }

        Category existingCategory = findEntityById(id);

        categoryMapper.updateEntity(existingCategory, dto);

        return categoryMapper.toResponse(categoryRepository.save(existingCategory));
    }

    /**
     * Deletes a category from the system.
     *
     * @param id the identifier of the category to be deleted
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = findEntityById(id);
        categoryRepository.delete(category);
    }

    /**
     * Retrieves a Category entity by its identifier.
     *
     * @param id the category identifier
     * @return the Category entity
     * @throws ResourceNotFoundException if the category does not exist
     */
    private Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found. ID = " + id));
    }

    /**
     * Validates whether a category name is already associated with another category.
     *
     * @param name the category name to be validated
     * @param categoryId the current category identifier, or null for creation
     * @throws BusinessException if the category name is already in use
     */
    private void validateNameUniqueness(String name, Long categoryId) {
        categoryRepository.findByName(name)
                .filter(existing -> categoryId == null || !existing.getId().equals(categoryId))
                .ifPresent(existing -> {
                    throw new BusinessException("Category name already in use.");
                });
    }

}
