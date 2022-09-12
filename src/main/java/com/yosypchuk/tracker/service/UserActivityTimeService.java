package com.yosypchuk.tracker.service;

import com.yosypchuk.tracker.model.DTO.UserActivityTimeDTO;
import com.yosypchuk.tracker.model.entity.UserActivityTime;

public interface UserActivityTimeService {
    UserActivityTime save(UserActivityTime time);

    UserActivityTime markTime(Long userId, Long activityId, UserActivityTimeDTO timeDTO);

    void delete(Long userId, Long activityId);
}
