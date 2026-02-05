package com.ems.finance_tracker.service;

import com.ems.finance_tracker.dto.CategoryDTO;
import com.ems.finance_tracker.exception.ResourceNotFoundException;
import com.ems.finance_tracker.model.entity.Category;
import com.ems.finance_tracker.model.mapper.CategoryMapper;
import com.ems.finance_tracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDTO.Response> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryDTO.Response findCategoryById(Long id) {
        Category category = findEntityById(id);

        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryDTO.Response saveCategory(CategoryDTO.Request dto) {
        Category category = categoryMapper.toEntity(dto);

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDTO.Response updateCategory(Long id, CategoryDTO.Update dto) {
        Category existingCategory = findEntityById(id);

        categoryMapper.updateEntity(existingCategory, dto);

        return categoryMapper.toResponse(categoryRepository.save(existingCategory));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = findEntityById(id);
        categoryRepository.delete(category);
    }

    private Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found. ID = " + id));
    }

}
