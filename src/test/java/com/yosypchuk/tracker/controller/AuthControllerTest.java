package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.configuration.security.jwt.JwtUtils;
import com.yosypchuk.tracker.model.DTO.UserDTO;
import com.yosypchuk.tracker.service.UserService;
import com.yosypchuk.tracker.testUtils.TestUserDataUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.yosypchuk.tracker.Utils.JsonMapper.objectToJson;
import static com.yosypchuk.tracker.testUtils.TestUserDataUtil.*;
import static com.yosypchuk.tracker.testUtils.TestUserDataUtil.MOCK_EMAIL;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfigurer.class),
        excludeAutoConfiguration = {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class}
)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationManager authenticationManager;

    private final static String AUTH_API = "/api/v1/auth";

    @Test
    void testRegister() throws Exception {
        UserDTO userBody = TestUserDataUtil.createUserDto();

        when(userService.registerUser(userBody)).thenReturn(userBody);

        mockMvc
                .perform(post(AUTH_API + "/register")
                        .content(objectToJson(userBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(MOCK_ID))
                .andExpect(jsonPath("$.firstName").value(MOCK_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(MOCK_LAST_NAME))
                .andExpect(jsonPath("$.email").value(MOCK_EMAIL))
                .andExpect(jsonPath("$.role").value(MOCK_ROLE.name()));

        verify(userService, times(1)).registerUser(userBody);
    }

    @Test
    void testLogin() throws Exception {
        UserDTO userDTO = TestUserDataUtil.createUserDto();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(MOCK_EMAIL, MOCK_PASSWORD);
        String jwt = "jwt";

        MockedStatic<JwtUtils> jwtUtils = Mockito.mockStatic(JwtUtils.class);
        jwtUtils.when(() -> JwtUtils.generateToken(token)).thenReturn(jwt);

        when(userService.getUserByEmail(MOCK_EMAIL)).thenReturn(userDTO);
        when(authenticationManager.authenticate(token)).thenReturn(token);

        mockMvc
                .perform(post(AUTH_API + "/login")
                        .content(objectToJson(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, jwt))
                .andExpect(jsonPath("$.email").value(MOCK_EMAIL));

        verify(authenticationManager, times(1)).authenticate(token);
        verify(userService, times(1)).getUserByEmail(MOCK_EMAIL);
    }

}
