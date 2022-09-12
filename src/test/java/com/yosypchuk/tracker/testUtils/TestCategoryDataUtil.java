package com.yosypchuk.tracker.testUtils;

import com.yosypchuk.tracker.model.DTO.CategoryDTO;
import com.yosypchuk.tracker.model.entity.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestCategoryDataUtil {
    public static final Long MOCK_ID = 1L;
    public static final String MOCK_NAME = "Activity category";

    public static Category createCategory() {
        return Category.builder()
                .id(MOCK_ID)
                .name(MOCK_NAME)
                .build();
    }

    public static CategoryDTO createCategoryDto() {
        return CategoryDTO.builder()
                .id(MOCK_ID)
                .name(MOCK_NAME)
                .build();
    }
}