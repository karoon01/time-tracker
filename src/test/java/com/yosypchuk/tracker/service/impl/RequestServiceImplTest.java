package com.yosypchuk.tracker.service.impl;

import com.yosypchuk.tracker.exception.EntityNotFoundException;
import com.yosypchuk.tracker.model.DTO.RequestDTO;
import com.yosypchuk.tracker.model.entity.Activity;
import com.yosypchuk.tracker.model.entity.Request;
import com.yosypchuk.tracker.model.entity.User;
import com.yosypchuk.tracker.model.entity.UserActivityTime;
import com.yosypchuk.tracker.repository.ActivityRepository;
import com.yosypchuk.tracker.repository.RequestRepository;
import com.yosypchuk.tracker.repository.TimeRepository;
import com.yosypchuk.tracker.repository.UserRepository;
import com.yosypchuk.tracker.testUtils.TestRequestDataUtil;
import com.yosypchuk.tracker.testUtils.TestUserActivityTimeDataUtil;
import com.yosypchuk.tracker.testUtils.TestUserDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private TimeRepository timeRepository;

    @Test
    void testCreateRequest() {
        //given
        Request expectedRequest = TestRequestDataUtil.createRequest();
        User requestUser = expectedRequest.getUser();
        Activity requestActivity = expectedRequest.getActivity();

        when(userRepository.findById(TestRequestDataUtil.MOCK_USER.getId())).thenReturn(Optional.of(requestUser));
        when(activityRepository.findById(TestRequestDataUtil.MOCK_ACTIVITY.getId())).thenReturn(Optional.of(requestActivity));
        when(requestRepository.save(any())).thenReturn(expectedRequest);

        //when
        RequestDTO actualRequest = requestService.createRequest(requestUser.getId(), requestActivity.getId());

        //then
        assertThat(actualRequest, allOf(
                hasProperty("status", equalTo(expectedRequest.getStatus())),
                hasProperty("user", equalTo(expectedRequest.getUser())),
                hasProperty("activity", equalTo(expectedRequest.getActivity()))
        ));
    }

    @Test
    void testCreateRequestThrowsExceptionIfUserDoesntExist() {
        //when
        when(userRepository.findById(TestRequestDataUtil.MOCK_USER.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> requestService.createRequest(TestRequestDataUtil.MOCK_USER.getId(), TestRequestDataUtil.MOCK_ACTIVITY.getId()));
    }

    @Test
    void testCreateRequestThrowsExceptionIfActivityDoesntExist() {
        //given
        User user = TestUserDataUtil.createUser();

        //when
        when(userRepository.findById(TestRequestDataUtil.MOCK_USER.getId())).thenReturn(Optional.of(user));
        when(activityRepository.findById(TestRequestDataUtil.MOCK_ACTIVITY.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> requestService.createRequest(TestRequestDataUtil.MOCK_USER.getId(), TestRequestDataUtil.MOCK_ACTIVITY.getId()));
    }

    @Test
    void testGetAllRequests() {
        //given
        Request request = TestRequestDataUtil.createRequest();
        List<Request> requestList = Collections.singletonList(request);
        when(requestRepository.findAll()).thenReturn(requestList);

        //when
        List<RequestDTO> requests = requestService.getAllRequests();

        //then
        assertThat(requests, hasSize(1));
    }

    @Test
    void testGetAllUserRequests() {
        //given
        Request request = TestRequestDataUtil.createRequest();
        User user = TestUserDataUtil.createUser();
        List<Request> userRequestsList = Collections.singletonList(request);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.getAllRequestsByUserId(user.getId())).thenReturn(userRequestsList);

        //when
        List<RequestDTO> requests = requestService.getAllUserRequests(user.getId());

        //then
        assertThat(requests, hasSize(1));
    }

    @Test
    void testGetAllUserRequestsThrowsExceptionIfUserDoesntExist() {
        //when
        when(userRepository.findById(TestRequestDataUtil.MOCK_USER.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> requestService.getAllUserRequests(TestRequestDataUtil.MOCK_USER.getId()));
    }

    @Test
    void testGetAllUserRequestsEmptyList() {
        //given
        User user = TestUserDataUtil.createUser();

        //when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.getAllRequestsByUserId(user.getId())).thenReturn(Collections.EMPTY_LIST);
        List<RequestDTO> requests = requestService.getAllUserRequests(user.getId());

        //then
        assertThat(requests, hasSize(0));
    }

    @Test
    void testApproveRequest() {
        //given
        Request request = TestRequestDataUtil.createRequest();
        UserActivityTime time = TestUserActivityTimeDataUtil.createUserActivityTime();

        when(requestRepository.findById(TestRequestDataUtil.MOCK_ID)).thenReturn(Optional.of(request));
        doNothing().when(requestRepository).updateRequestStatusById(TestRequestDataUtil.MOCK_APPROVED_STATUS, TestRequestDataUtil.MOCK_ID);
        when(timeRepository.save(any())).thenReturn(time);

        //when
        requestService.approveRequest(TestRequestDataUtil.MOCK_ID);

        //then
        verify(requestRepository, times(1)).updateRequestStatusById(TestRequestDataUtil.MOCK_APPROVED_STATUS, TestRequestDataUtil.MOCK_ID);
    }

    @Test
    void testApproveRequestThrowsExceptionIfRequestDoesntExist() {
        //when
        when(requestRepository.findById(TestRequestDataUtil.MOCK_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> requestService.approveRequest(TestRequestDataUtil.MOCK_ID));
    }

    @Test
    void testRejectRequest() {
        //given
        Request request = TestRequestDataUtil.createRequest();
        when(requestRepository.findById(TestRequestDataUtil.MOCK_ID)).thenReturn(Optional.of(request));
        doNothing().when(requestRepository).updateRequestStatusById(TestRequestDataUtil.MOCK_REJECTED_STATUS, TestRequestDataUtil.MOCK_ID);

        //when
        requestService.rejectRequest(TestRequestDataUtil.MOCK_ID);

        //then
        verify(requestRepository, times(1)).updateRequestStatusById(TestRequestDataUtil.MOCK_REJECTED_STATUS, TestRequestDataUtil.MOCK_ID);
    }

    @Test
    void testRejectRequestThrowsExceptionIfRequestDoesntExist() {
        //when
        when(requestRepository.findById(TestRequestDataUtil.MOCK_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> requestService.rejectRequest(TestRequestDataUtil.MOCK_ID));
    }

    @Test
    void testDeleteRequest() {
        //given
        Request request = TestRequestDataUtil.createRequest();
        when(requestRepository.findById(TestRequestDataUtil.MOCK_ID)).thenReturn(Optional.of(request));
        doNothing().when(requestRepository).delete(any());

        //when
        requestService.deleteRequest(TestRequestDataUtil.MOCK_ID);

        //then
        verify(requestRepository, times(1)).delete(request);
    }

    @Test
    void testDeleteRequestThrowsExceptionIfRequestDoesntExist() {
        //when
        when(requestRepository.findById(TestRequestDataUtil.MOCK_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> requestService.deleteRequest(TestRequestDataUtil.MOCK_ID));
    }
}