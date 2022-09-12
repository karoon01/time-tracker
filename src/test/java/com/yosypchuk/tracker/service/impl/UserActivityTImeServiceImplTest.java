package com.yosypchuk.tracker.service.impl;

import com.yosypchuk.tracker.exception.EntityNotFoundException;
import com.yosypchuk.tracker.model.DTO.UserActivityTimeDTO;
import com.yosypchuk.tracker.model.entity.Activity;
import com.yosypchuk.tracker.model.entity.User;
import com.yosypchuk.tracker.model.entity.UserActivityTime;
import com.yosypchuk.tracker.repository.ActivityRepository;
import com.yosypchuk.tracker.repository.TimeRepository;
import com.yosypchuk.tracker.repository.UserRepository;
import com.yosypchuk.tracker.testUtils.TestActivityDataUtil;
import com.yosypchuk.tracker.testUtils.TestUserActivityTimeDataUtil;
import com.yosypchuk.tracker.testUtils.TestUserDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserActivityTImeServiceImplTest {
    @InjectMocks
    private UserActivityTimeServiceImpl timeService;

    @Mock
    private TimeRepository timeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ActivityRepository activityRepository;

    @Test
    void testSave() {
        //given
        UserActivityTime expectedTime = TestUserActivityTimeDataUtil.createUserActivityTime();
        when(timeRepository.save(any())).thenReturn(expectedTime);

        //when
        UserActivityTime actualTime = timeService.save(expectedTime);

        //then
        assertThat(actualTime, allOf(
                hasProperty("user", equalTo(expectedTime.getUser())),
                hasProperty("activity", equalTo(expectedTime.getActivity())),
                hasProperty("duration", equalTo(expectedTime.getDuration()))
        ));
    }

    @Test
    void testMarkTime() {
        //given
        UserActivityTime time = TestUserActivityTimeDataUtil.createUserActivityTime();
        UserActivityTimeDTO timeBody = TestUserActivityTimeDataUtil.createUserActivityTimeDto();
        User user = time.getUser();
        Activity activity = time.getActivity();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        when(timeRepository.findByActivityIdAndUserId(activity.getId(), user.getId())).thenReturn(Optional.of(time));
        doNothing().when(timeRepository).markTime(TestUserActivityTimeDataUtil.MOCK_SESSION_DURATION, activity.getId(), user.getId());

        //when
        timeService.markTime(user.getId(), activity.getId(), timeBody);

        //then
        verify(timeRepository, times(1)).markTime(TestUserActivityTimeDataUtil.MOCK_SESSION_DURATION, activity.getId(), user.getId());
    }

    @Test
    void testMarkTimeThrowsExceptionIfUserDoesntExist() {
        //given
        UserActivityTimeDTO timeBody = TestUserActivityTimeDataUtil.createUserActivityTimeDto();

        //when
        when(userRepository.findById(TestUserActivityTimeDataUtil.MOCK_USER.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> timeService.markTime(TestUserActivityTimeDataUtil.MOCK_USER.getId(), TestUserActivityTimeDataUtil.MOCK_ACTIVITY.getId(), timeBody));
    }

    @Test
    void testMarkTimeThrowsExceptionIfActivityDoesntExist() {
        //given
        UserActivityTimeDTO timeBody = TestUserActivityTimeDataUtil.createUserActivityTimeDto();
        User user = TestUserDataUtil.createUser();

        //when
        when(userRepository.findById(TestUserActivityTimeDataUtil.MOCK_USER.getId())).thenReturn(Optional.of(user));
        when(activityRepository.findById(TestUserActivityTimeDataUtil.MOCK_ACTIVITY.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> timeService.markTime(TestUserActivityTimeDataUtil.MOCK_USER.getId(), TestUserActivityTimeDataUtil.MOCK_ACTIVITY.getId(), timeBody));
    }

    @Test
    void testMarkTimeThrowsExceptionIfTimeDoesntExist() {
        //given
        UserActivityTimeDTO timeBody = TestUserActivityTimeDataUtil.createUserActivityTimeDto();
        User user = TestUserDataUtil.createUser();
        Activity activity = TestActivityDataUtil.createActivity();

        //when
        when(userRepository.findById(TestUserActivityTimeDataUtil.MOCK_USER.getId())).thenReturn(Optional.of(user));
        when(activityRepository.findById(TestUserActivityTimeDataUtil.MOCK_ACTIVITY.getId())).thenReturn(Optional.of(activity));
        when(timeRepository.findByActivityIdAndUserId(TestUserActivityTimeDataUtil.MOCK_ACTIVITY.getId(), TestUserActivityTimeDataUtil.MOCK_USER.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> timeService.markTime(TestUserActivityTimeDataUtil.MOCK_USER.getId(), TestUserActivityTimeDataUtil.MOCK_ACTIVITY.getId(), timeBody));
    }

    @Test
    void testDelete() {
        //given
        UserActivityTime expectedTime = TestUserActivityTimeDataUtil.createUserActivityTime();
        User user = expectedTime.getUser();
        Activity activity = expectedTime.getActivity();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        doNothing().when(timeRepository).deleteByUserIdAndActivityId(user.getId(), activity.getId());

        //when
        timeService.delete(user.getId(), activity.getId());

        //then
        verify(timeRepository, times(1)).deleteByUserIdAndActivityId(user.getId(), activity.getId());
    }

    @Test
    void testDeleteThrowsExceptionIfUserDoesntExist() {
        //when
        when(userRepository.findById(TestUserActivityTimeDataUtil.MOCK_USER.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> timeService.delete(TestUserActivityTimeDataUtil.MOCK_USER.getId(), TestUserActivityTimeDataUtil.MOCK_ACTIVITY.getId()));

    }

    @Test
    void testDeleteThrowsExceptionIfActivityDoesntExist() {
        //given
        User user = TestUserDataUtil.createUser();

        //when
        when(userRepository.findById(TestUserActivityTimeDataUtil.MOCK_USER.getId())).thenReturn(Optional.of(user));
        when(activityRepository.findById(TestUserActivityTimeDataUtil.MOCK_ACTIVITY.getId())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> timeService.delete(TestUserActivityTimeDataUtil.MOCK_USER.getId(), TestUserActivityTimeDataUtil.MOCK_ACTIVITY.getId()));
    }
}