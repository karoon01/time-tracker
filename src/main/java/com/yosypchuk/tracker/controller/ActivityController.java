package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.api.ActivityApi;
import com.yosypchuk.tracker.model.DTO.ActivityDTO;
import com.yosypchuk.tracker.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController implements ActivityApi {

    private final ActivityService activityService;

    @Override
    public ActivityDTO getActivity(@PathVariable Long id) {
        return activityService.getActivity(id);
    }

    @Override
    public List<ActivityDTO> getAllActivities() {
        return activityService.getAllActivities();
    }

    @Override
    public ActivityDTO createActivity(@Valid @RequestBody ActivityDTO activityDTO) {
        return activityService.createActivity(activityDTO);
    }

    @Override
    public ActivityDTO updateActivity(@PathVariable Long id, @Valid @RequestBody ActivityDTO activityDTO) {
        return activityService.updateActivity(id, activityDTO);
    }

    @Override
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}
