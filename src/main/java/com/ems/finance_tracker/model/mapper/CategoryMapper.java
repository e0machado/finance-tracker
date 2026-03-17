package com.ems.finance_tracker.model.mapper;

import com.ems.finance_tracker.dto.CategoryDTO;
import com.ems.finance_tracker.model.entity.Category;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting {@link CategoryDTO} to {@link Category} entities
 * and vice versa.
 *
 * @author Evandro Machado
 */
@Component
public class CategoryMapper {

    /**
     * Converts a category creation request DTO into a Category entity.
     *
     * @param dto the category creation request data
     * @return a new Category entity ready for persistence
     */
    public Category toEntity(CategoryDTO.Request dto) {
        return Category.builder()
                .name(dto.name())
                .build();
    }

    /**
     * Converts a Category entity into a response DTO.
     *
     * @param category the persisted category entity
     * @return a response DTO exposing category data
     */
    public CategoryDTO.Response toResponse(Category category) {
        return new CategoryDTO.Response(
                category.getId(),
                category.getName()
        );
    }

    /**
     * Updates mutable fields of an existing Category entity
     * using data from an update DTO.
     *
     * <p>This method mutates the provided entity directly.</p>
     *
     * @param category the existing category entity to be updated
     * @param dto the DTO containing updated category information
     */
    public void updateEntity(Category category, CategoryDTO.Update dto) {
        dto.name().ifPresent(category::setName);
    }

}
