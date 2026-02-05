package com.ems.finance_tracker.controller;

import com.ems.finance_tracker.dto.CategoryDTO;
import com.ems.finance_tracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO.Response>> findAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO.Response> findCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO.Response> createCategory(@Valid @RequestBody CategoryDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.saveCategory(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO.Response> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO.Update dto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
