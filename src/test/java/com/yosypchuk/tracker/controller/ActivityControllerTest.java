package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.model.DTO.ActivityDTO;
import com.yosypchuk.tracker.service.ActivityService;
import com.yosypchuk.tracker.testUtils.TestActivityDataUtil;
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
import static com.yosypchuk.tracker.testUtils.TestActivityDataUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(
        controllers = ActivityController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfigurer.class),
        excludeAutoConfiguration = {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class}
)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
public class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    private final static String ACTIVITY_API = "/api/v1/activity";

    @Test
    void testCreateActivity() throws Exception {
        ActivityDTO activityBody = TestActivityDataUtil.createActivityDto();

        when(activityService.createActivity(any())).thenReturn(activityBody);

        mockMvc.perform(post(ACTIVITY_API)
                        .content(objectToJson(activityBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(MOCK_ID))
                .andExpect(jsonPath("$.name").value(MOCK_NAME))
                .andExpect(jsonPath("$.description").value(MOCK_DESCRIPTION))
                .andExpect(jsonPath("$.category").value(MOCK_CATEGORY));

        verify(activityService, times(1)).createActivity(activityBody);
    }

    @Test
    void testGetActivity() throws Exception {
        ActivityDTO activity = TestActivityDataUtil.createActivityDto();

        when(activityService.getActivity(MOCK_ID)).thenReturn(activity);

        mockMvc
                .perform(get(ACTIVITY_API + "/" + MOCK_ID)
                        .content(objectToJson(activity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(MOCK_ID))
                .andExpect(jsonPath("$.name").value(MOCK_NAME))
                .andExpect(jsonPath("$.description").value(MOCK_DESCRIPTION))
                .andExpect(jsonPath("$.category").value(MOCK_CATEGORY));

        verify(activityService, times(1)).getActivity(MOCK_ID);
    }

    @Test
    void testGetAllActivities() throws Exception {
        ActivityDTO activity = TestActivityDataUtil.createActivityDto();
        List<ActivityDTO> userList = Collections.singletonList(activity);

        when(activityService.getAllActivities()).thenReturn(userList);

        mockMvc
                .perform(get(ACTIVITY_API + "/all")
                        .content(objectToJson(activity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(MOCK_ID))
                .andExpect(jsonPath("$[0].name").value(MOCK_NAME))
                .andExpect(jsonPath("$[0].description").value(MOCK_DESCRIPTION))
                .andExpect(jsonPath("$[0].category").value(MOCK_CATEGORY));
    }

    @Test
    void testUpdateActivity() throws Exception {
        ActivityDTO updateBody = TestActivityDataUtil.createUpdatedActivityDto();

        when(activityService.updateActivity(MOCK_ID, updateBody)).thenReturn(updateBody);

        mockMvc.perform(put(ACTIVITY_API + "/" + MOCK_ID)
                        .content(objectToJson(updateBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(MOCK_ID))
                .andExpect(jsonPath("$.name").value(MOCK_UPDATE_NAME))
                .andExpect(jsonPath("$.description").value(MOCK_UPDATE_DESCRIPTION))
                .andExpect(jsonPath("$.category").value(MOCK_CATEGORY));

        verify(activityService, times(1)).updateActivity(MOCK_ID, updateBody);
    }

    @Test
    void testDeleteActivity() throws Exception {
        doNothing().when(activityService).deleteActivity(MOCK_ID);

        mockMvc
                .perform(delete(ACTIVITY_API + "/" + MOCK_ID))
                .andExpect(status().isNoContent());

        verify(activityService, times(1)).deleteActivity(MOCK_ID);
    }
}
