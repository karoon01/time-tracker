package com.yosypchuk.tracker.service.impl;

import com.yosypchuk.tracker.exception.EntityAlreadyExistException;
import com.yosypchuk.tracker.model.DTO.CategoryDTO;
import com.yosypchuk.tracker.exception.EntityNotFoundException;
import com.yosypchuk.tracker.model.entity.Category;
import com.yosypchuk.tracker.repository.CategoryRepository;
import com.yosypchuk.tracker.service.CategoryService;
import com.yosypchuk.tracker.mapper.CategoryMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        String name = categoryDTO.getName();
        Optional<Category> possibleCategory = categoryRepository.findCategoryByName(name);

        if(possibleCategory.isPresent()) {
            log.warn("Category with name: \"{}\" already exist!", name);
            throw new EntityAlreadyExistException("Activity category is already exist!");
        }

        log.info("Create category with name: {}", name);
        Category category = CategoryMapper.INSTANCE.mapCategory(categoryDTO);
        categoryRepository.save(category);

        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        log.info("Get all categories");
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper.INSTANCE::mapCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategory(String name) {
        log.info("Get category by name: {}", name);
        Category category = categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Category doesn't exist"));

        return CategoryMapper.INSTANCE.mapCategoryDto(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        log.info("Delete category with id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category doesn't exist"));
        categoryRepository.delete(category);
    }
}
