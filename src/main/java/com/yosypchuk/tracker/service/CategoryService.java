package com.yosypchuk.tracker.service;

import com.yosypchuk.tracker.model.DTO.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategory(String name);

    void deleteCategory(Long id);
}
