package com.yosypchuk.tracker.service;

import com.yosypchuk.tracker.model.DTO.ActivityDTO;

import java.util.List;

public interface ActivityService {
    ActivityDTO createActivity(ActivityDTO activityDTO);

    ActivityDTO getActivity(Long id);

    ActivityDTO updateActivity(Long id, ActivityDTO activityDTO);

    void deleteActivity(Long id);

    List<ActivityDTO> getAllActivities();
}
