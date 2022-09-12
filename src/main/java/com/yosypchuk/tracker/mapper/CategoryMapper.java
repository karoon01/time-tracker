package com.yosypchuk.tracker.mapper;

import com.yosypchuk.tracker.model.DTO.CategoryDTO;
import com.yosypchuk.tracker.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category mapCategory(CategoryDTO categoryDTO);
    CategoryDTO mapCategoryDto(Category category);
}
