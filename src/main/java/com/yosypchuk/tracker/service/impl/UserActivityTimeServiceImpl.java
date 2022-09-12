package com.yosypchuk.tracker.service.impl;

import com.yosypchuk.tracker.model.DTO.UserActivityTimeDTO;
import com.yosypchuk.tracker.exception.EntityNotFoundException;
import com.yosypchuk.tracker.model.entity.Activity;
import com.yosypchuk.tracker.model.entity.User;
import com.yosypchuk.tracker.model.entity.UserActivityTime;
import com.yosypchuk.tracker.model.entity.UserActivityTimeKey;
import com.yosypchuk.tracker.repository.ActivityRepository;
import com.yosypchuk.tracker.repository.TimeRepository;
import com.yosypchuk.tracker.repository.UserRepository;
import com.yosypchuk.tracker.service.UserActivityTimeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;

@Slf4j
@Service
@AllArgsConstructor
public class UserActivityTimeServiceImpl implements UserActivityTimeService {

    private final TimeRepository timeRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    @Override
    public UserActivityTime save(UserActivityTime time) {
        log.info("Save UserActivityTime: {}", time);
        return timeRepository.save(time);
    }

    @Transactional
    @Override
    public UserActivityTime markTime(Long userId, Long activityId, UserActivityTimeDTO timeDTO) {
        LocalTime startTime = LocalTime.parse(timeDTO.getStartTime());
        LocalTime endTime = LocalTime.parse(timeDTO.getEndTime());
        Duration sessionDuration = Duration.between(startTime, endTime);

        log.info("Get user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User is not found!"));

        log.info("Get activity with id: {}", activityId);
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity is not found!"));

        log.info("Get UserActivityTime for user with id: {} for activity with id: {}", user.getId(), activity.getId());
        UserActivityTime time = timeRepository.findByActivityIdAndUserId(activity.getId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User activity time is not found!"));

        String updatedDuration = Duration.parse(time.getDuration()).plus(sessionDuration).toString();

        UserActivityTime updatedTime = UserActivityTime.builder()
                .id(new UserActivityTimeKey(activity.getId(), user.getId()))
                .user(user)
                .activity(activity)
                .duration(updatedDuration)
                .build();

        log.info("Mark time for user with id: {} for activity with id: {}", user.getId(), activity.getId());
        timeRepository.markTime(updatedDuration, activity.getId(), user.getId());

        return updatedTime;
    }

    @Override
    public void delete(Long userId, Long activityId) {
        log.info("Delete UserActivityTime for user with id: {} and activity with id: {}", userId, activityId);
        userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException("User is not found"));
        activityRepository.findById(activityId)
                        .orElseThrow(() -> new EntityNotFoundException("Activity is not found"));

        timeRepository.deleteByUserIdAndActivityId(userId, activityId);
    }
}
