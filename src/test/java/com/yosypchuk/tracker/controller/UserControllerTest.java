package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.model.DTO.UserDTO;
import com.yosypchuk.tracker.service.UserActivityTimeService;
import com.yosypchuk.tracker.service.UserService;
import com.yosypchuk.tracker.testUtils.TestUserDataUtil;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.yosypchuk.tracker.Utils.JsonMapper.objectToJson;
import static com.yosypchuk.tracker.testUtils.TestUserDataUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfigurer.class),
        excludeAutoConfiguration = {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class}
)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserActivityTimeService timeService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    private static final String USER_API = "/api/v1/user";

    @Test
    void testGetAllUsers() throws Exception {
        UserDTO user = TestUserDataUtil.createUserDto();
        List<UserDTO> userList = Collections.singletonList(user);

        when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get(USER_API + "/all")
                        .content(objectToJson(userList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(MOCK_ID))
                .andExpect(jsonPath("$[0].firstName").value(MOCK_FIRST_NAME))
                .andExpect(jsonPath("$[0].lastName").value(MOCK_LAST_NAME))
                .andExpect(jsonPath("$[0].email").value(MOCK_EMAIL))
                .andExpect(jsonPath("$[0].role").value(MOCK_ROLE.name()));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() throws Exception {
        UserDTO userDto = TestUserDataUtil.createUserDto();

        when(userService.getUser(MOCK_ID)).thenReturn(userDto);

        mockMvc
                .perform(get(USER_API + "/" + MOCK_ID)
                        .content(objectToJson(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(MOCK_ID))
                .andExpect(jsonPath("$.firstName").value(MOCK_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(MOCK_LAST_NAME))
                .andExpect(jsonPath("$.email").value(MOCK_EMAIL))
                .andExpect(jsonPath("$.role").value(MOCK_ROLE.name()));

        verify(userService, times(1)).getUser(MOCK_ID);
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDTO updateBody = TestUserDataUtil.createUpdatedUserDto();

        when(userService.updateUser(MOCK_ID, updateBody)).thenReturn(updateBody);

        mockMvc
                .perform(put(USER_API + "/" + MOCK_ID)
                        .content(objectToJson(updateBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(MOCK_ID))
                .andExpect(jsonPath("$.firstName").value(MOCK_UPDATE_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(MOCK_UPDATE_LAST_NAME))
                .andExpect(jsonPath("$.email").value(MOCK_UPDATE_EMAIL))
                .andExpect(jsonPath("$.role").value(MOCK_ROLE.name()));

        verify(userService).updateUser(MOCK_ID, updateBody);
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(MOCK_ID);

        mockMvc
                .perform(delete(USER_API + "/" + MOCK_ID))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(MOCK_ID);
    }
}
