package com.yosypchuk.tracker.controller;

import com.yosypchuk.tracker.api.AuthApi;
import com.yosypchuk.tracker.configuration.security.jwt.JwtUtils;
import com.yosypchuk.tracker.model.DTO.LoginRequestDTO;
import com.yosypchuk.tracker.model.DTO.UserDTO;
import com.yosypchuk.tracker.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
public class AuthController implements AuthApi {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDTO register(UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @Override
    public ResponseEntity<UserDTO> login(LoginRequestDTO request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String jwtToken = JwtUtils.generateToken(authentication);
        UserDTO userDTO = userService.getUserByEmail(authentication.getName());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwtToken).body(userDTO);
    }

}
