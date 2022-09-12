package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.model.DTO.CategoryDTO;
import com.yosypchuk.tracker.service.CategoryService;
import com.yosypchuk.tracker.testUtils.TestCategoryDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.yosypchuk.tracker.Utils.JsonMapper.objectToJson;
import static com.yosypchuk.tracker.testUtils.TestCategoryDataUtil.MOCK_ID;
import static com.yosypchuk.tracker.testUtils.TestCategoryDataUtil.MOCK_NAME;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(
        controllers = CategoryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfigurer.class),
        excludeAutoConfiguration = {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class}
)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private static final String CATEGORY_API = "/api/v1/activity/category";

    @Test
    void testGetAllActivityCategories() throws Exception {
        CategoryDTO category = TestCategoryDataUtil.createCategoryDto();
        List<CategoryDTO> categoryList = Collections.singletonList(category);

        when(categoryService.getAllCategories()).thenReturn(categoryList);

        mockMvc
                .perform(get(CATEGORY_API + "/all")
                        .content(objectToJson(categoryList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(MOCK_ID))
                .andExpect(jsonPath("$[0].name").value(MOCK_NAME));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetActivityCategory() throws Exception {
        CategoryDTO category = TestCategoryDataUtil.createCategoryDto();

        when(categoryService.getCategory(MOCK_NAME)).thenReturn(category);

        mockMvc
                .perform(get(CATEGORY_API + "/" + MOCK_NAME)
                        .content(objectToJson(category))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(MOCK_ID))
                .andExpect(jsonPath("$.name").value(MOCK_NAME));

        verify(categoryService, times(1)).getCategory(MOCK_NAME);
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryDTO category = TestCategoryDataUtil.createCategoryDto();

        when(categoryService.createCategory(category)).thenReturn(category);

        mockMvc
                .perform(post(CATEGORY_API)
                        .content(objectToJson(category))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(MOCK_ID))
                .andExpect(jsonPath("$.name").value(MOCK_NAME));

        verify(categoryService, times(1)).createCategory(category);
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(MOCK_ID);

        mockMvc
                .perform(delete(CATEGORY_API + "/" + MOCK_ID))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(MOCK_ID);
    }
}
