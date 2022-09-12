package com.yosypchuk.tracker.api;

import com.yosypchuk.tracker.model.DTO.LoginRequestDTO;
import com.yosypchuk.tracker.model.DTO.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api("Authentication and authorization management API")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @ApiOperation("Register user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 409, message = "Conflict")
    })
    UserDTO register(@RequestBody @Valid UserDTO userDTO);

    @ApiOperation("Sign in user")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<UserDTO> login(@RequestBody @Valid LoginRequestDTO request);
}
