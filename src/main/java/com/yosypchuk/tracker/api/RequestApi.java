package com.yosypchuk.tracker.api;

import com.yosypchuk.tracker.model.DTO.RequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Request management API")
@RequestMapping("/api/v1/request")
public interface RequestApi {

    @ApiOperation("Get all requests")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @ApiResponse(code = 200, message = "OK")
    List<RequestDTO> getAllRequests();

    @ApiOperation("Get user's requests")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/user/{id}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    List<RequestDTO> getAllUsersRequests(@PathVariable Long id);

    @ApiOperation("Create request")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/{userId}/activity/{activityId}")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    RequestDTO createRequest(@PathVariable Long userId, @PathVariable Long activityId);

    @ApiOperation("Delete request")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<Void> deleteRequest(@PathVariable Long id);

    @ApiOperation("Approve request by id")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/approve")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<Void> approveRequest(@PathVariable Long id);

    @ApiOperation("Reject request by id")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/reject")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<Void> rejectRequest(@PathVariable Long id);
}