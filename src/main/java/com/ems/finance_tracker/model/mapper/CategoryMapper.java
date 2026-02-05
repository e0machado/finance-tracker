package com.ems.finance_tracker.model.mapper;

import com.ems.finance_tracker.dto.CategoryDTO;
import com.ems.finance_tracker.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryDTO.Request dto) {
        return Category.builder()
                .name(dto.name())
                .build();
    }

    public CategoryDTO.Response toResponse(Category category) {
        return new CategoryDTO.Response(
                category.getId(),
                category.getName()
        );
    }

    public void updateEntity(Category category, CategoryDTO.Update dto) {
        dto.name().ifPresent(category::setName);
    }

}
