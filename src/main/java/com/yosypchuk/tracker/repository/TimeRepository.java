package com.yosypchuk.tracker.repository;

import com.yosypchuk.tracker.model.entity.UserActivityTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TimeRepository extends JpaRepository<UserActivityTime, Long> {

    @Modifying
    @Query("UPDATE UserActivityTime u SET u.duration=?1 WHERE u.activity.id=?2 AND u.user.id=?3")
    void markTime(String duration, Long activityId, Long userId);

    Optional<UserActivityTime> findByActivityIdAndUserId(Long activityId, Long userId);

    void deleteByUserIdAndActivityId(Long userId, Long activityId);
}
