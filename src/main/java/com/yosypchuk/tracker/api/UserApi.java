package com.yosypchuk.tracker.api;

import com.yosypchuk.tracker.model.DTO.UserActivityTimeDTO;
import com.yosypchuk.tracker.model.DTO.UserDTO;
import com.yosypchuk.tracker.model.entity.UserActivityTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "User management API")
@RequestMapping("/api/v1/user")
public interface UserApi {

    @ApiOperation("Get all users")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @ApiResponse(code = 200, message = "OK")
    List<UserDTO> getAllUsers();

    @ApiOperation("Get user by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    UserDTO getUserById(@PathVariable Long id);

    @ApiOperation("Update user")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO);

    @ApiOperation("Delete user")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<Void> deleteUser(@PathVariable Long id);

    @ApiOperation("Mark time for activity")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userId}/activity/{activityId}/mark")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    UserActivityTime markTime(@PathVariable Long userId, @PathVariable Long activityId, @RequestBody @Valid UserActivityTimeDTO timeDTO);
}
