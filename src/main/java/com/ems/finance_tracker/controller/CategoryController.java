package com.ems.finance_tracker.controller;

import com.ems.finance_tracker.dto.CategoryDTO;
import com.ems.finance_tracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling {@link com.ems.finance_tracker.model.entity.Category}
 * related HTTP requests.
 * Provides CRUD operations for categories.
 *
 * @author Evandro Machado
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Retrieves all categories.
     *
     * @return HTTP 200 OK with a list of {@link CategoryDTO.Response} representing all categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO.Response>> findAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    /**
     * Retrieves a single category by ID.
     *
     * @param id the category identifier
     * @return HTTP 200 OK with a {@link CategoryDTO.Response} containing category data
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the category does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO.Response> findCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    /**
     * Creates a new category.
     *
     * @param dto the {@link CategoryDTO.Request} containing category creation data
     * @return HTTP 201 Created with a {@link CategoryDTO.Response} representing the persisted category
     * @throws com.ems.finance_tracker.exception.BusinessException if the category name is already in use
     */
    @PostMapping
    public ResponseEntity<CategoryDTO.Response> createCategory(@Valid @RequestBody CategoryDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.saveCategory(dto));
    }

    /**
     * Updates an existing category's information.
     *
     * @param id the identifier of the category to be updated
     * @param dto the {@link CategoryDTO.Update} containing updated category data
     * @return HTTP 200 OK with a {@link CategoryDTO.Response} representing the updated category
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the category does not exist
     * @throws com.ems.finance_tracker.exception.BusinessException if the category name is already in use by another category
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDTO.Response> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO.Update dto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    /**
     * Deletes a category by ID.
     *
     * @param id the identifier of the category to be deleted
     * @return HTTP 204 No Content if deletion is successful
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the category does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
