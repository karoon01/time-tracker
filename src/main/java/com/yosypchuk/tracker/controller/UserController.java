package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.api.UserApi;
import com.yosypchuk.tracker.model.DTO.UserActivityTimeDTO;
import com.yosypchuk.tracker.model.DTO.UserDTO;
import com.yosypchuk.tracker.model.entity.UserActivityTime;
import com.yosypchuk.tracker.service.UserActivityTimeService;
import com.yosypchuk.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    private final UserActivityTimeService timeService;

    @Override
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @Override
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @Override
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public UserActivityTime markTime(@PathVariable Long userId, @PathVariable Long activityId, @Valid @RequestBody UserActivityTimeDTO timeDTO) {
        return timeService.markTime(userId, activityId, timeDTO);
    }
}
