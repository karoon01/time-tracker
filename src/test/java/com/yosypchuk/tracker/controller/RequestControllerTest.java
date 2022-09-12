package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.model.DTO.RequestDTO;
import com.yosypchuk.tracker.service.RequestService;
import com.yosypchuk.tracker.testUtils.TestRequestDataUtil;
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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import static com.yosypchuk.tracker.Utils.JsonMapper.objectToJson;
import static com.yosypchuk.tracker.testUtils.TestRequestDataUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(
        controllers = RequestController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfigurer.class),
        excludeAutoConfiguration = {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class}
)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
public class RequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    private final static String REQUEST_API = "/api/v1/request";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Test
    void testGetAllRequests() throws Exception {
        RequestDTO request = TestRequestDataUtil.createRequestDto();
        List<RequestDTO> requestList = Collections.singletonList(request);

        when(requestService.getAllRequests()).thenReturn(requestList);

        mockMvc
                .perform(get(REQUEST_API + "/all")
                        .content(objectToJson(requestList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(MOCK_ID))
                .andExpect(jsonPath("$[0].requestDate").value(dateFormat.format(MOCK_REQUEST_DATE)))
                .andExpect(jsonPath("$[0].status").value(MOCK_STATUS.name()))
                .andExpect(jsonPath("$[0].user").value(MOCK_USER))
                .andExpect(jsonPath("$[0].activity").value(MOCK_ACTIVITY));

        verify(requestService, times(1)).getAllRequests();
    }

    @Test
    void testGetAllUsersRequests() throws Exception {
        RequestDTO request = TestRequestDataUtil.createRequestDto();
        List<RequestDTO> requestList = Collections.singletonList(request);

        when(requestService.getAllUserRequests(MOCK_USER.getId())).thenReturn(requestList);

        mockMvc
                .perform(get(REQUEST_API + "/all/user/" + MOCK_USER.getId())
                        .content(objectToJson(requestList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(MOCK_ID))
                .andExpect(jsonPath("$[0].requestDate").value(dateFormat.format(MOCK_REQUEST_DATE)))
                .andExpect(jsonPath("$[0].status").value(MOCK_STATUS.name()))
                .andExpect(jsonPath("$[0].user").value(MOCK_USER))
                .andExpect(jsonPath("$[0].activity").value(MOCK_ACTIVITY));

        verify(requestService, times(1)).getAllUserRequests(MOCK_USER.getId());
    }

    @Test
    void testCreateRequest() throws Exception {
        RequestDTO request = TestRequestDataUtil.createRequestDto();

        when(requestService.createRequest(MOCK_USER.getId(), MOCK_ACTIVITY.getId())).thenReturn(request);

        mockMvc
                .perform(post(REQUEST_API + "/user/" + MOCK_USER.getId() + "/activity/" + MOCK_ACTIVITY.getId())
                        .content(objectToJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(MOCK_ID))
                .andExpect(jsonPath("$.requestDate").value(dateFormat.format(MOCK_REQUEST_DATE)))
                .andExpect(jsonPath("$.status").value(MOCK_STATUS.name()))
                .andExpect(jsonPath("$.user").value(MOCK_USER))
                .andExpect(jsonPath("$.activity").value(MOCK_ACTIVITY));

        verify(requestService, times(1)).createRequest(MOCK_USER.getId(), MOCK_ACTIVITY.getId());
    }

    @Test
    void testDeleteActivity() throws Exception {
        doNothing().when(requestService).deleteRequest(MOCK_ID);

        mockMvc
                .perform(delete(REQUEST_API + "/" + MOCK_ID))
                .andExpect(status().isNoContent());

        verify(requestService, times(1)).deleteRequest(MOCK_ID);
    }

    @Test
    void testApproveRequest() throws Exception {
        doNothing().when(requestService).approveRequest(MOCK_ID);

        mockMvc
                .perform(put(REQUEST_API + "/" + MOCK_ID + "/approve"))
                .andExpect(status().isNoContent());

        verify(requestService, times(1)).approveRequest(MOCK_ID);
    }

    @Test
    void testRejectRequest() throws Exception {
        doNothing().when(requestService).rejectRequest(MOCK_ID);

        mockMvc
                .perform(put(REQUEST_API + "/" + MOCK_ID + "/reject"))
                .andExpect(status().isNoContent());

        verify(requestService, times(1)).rejectRequest(MOCK_ID);
    }
}
