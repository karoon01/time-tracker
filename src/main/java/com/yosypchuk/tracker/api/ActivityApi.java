package com.yosypchuk.tracker.api;

import com.yosypchuk.tracker.model.DTO.ActivityDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Activity management API")
@RequestMapping("/api/v1/activity")
public interface ActivityApi {

    @ApiOperation("Get activity by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    ActivityDTO getActivity(@PathVariable Long id);

    @ApiOperation("Get all activities")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @ApiResponse(code = 200, message = "OK")
    List<ActivityDTO> getAllActivities();

    @ApiOperation("Create activity")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 409, message = "Conflict")
    })
    ActivityDTO createActivity(@RequestBody @Valid ActivityDTO activityDTO);

    @ApiOperation("Update activity")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    ActivityDTO updateActivity(@PathVariable Long id, @RequestBody @Valid ActivityDTO activityDTO);

    @ApiOperation("Delete activity")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<Void> deleteActivity(@PathVariable Long id);
}
