package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.api.CategoryApi;
import com.yosypchuk.tracker.model.DTO.CategoryDTO;
import com.yosypchuk.tracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Override
    public List<CategoryDTO> getAllActivityCategories() {
        return categoryService.getAllCategories();
    }

    @Override
    public CategoryDTO getActivityCategory(@PathVariable String name) {
        return categoryService.getCategory(name);
    }

    @Override
    public CategoryDTO createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    @Override
    public ResponseEntity<Void> removeCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
