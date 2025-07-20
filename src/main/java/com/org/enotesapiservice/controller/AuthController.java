package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.dto.LoginRequest;
import com.org.enotesapiservice.dto.LoginResponse;
import com.org.enotesapiservice.dto.UserRequest;
import com.org.enotesapiservice.service.AuthService;
import com.org.enotesapiservice.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request) throws Exception {
        log.info("AuthController : registerUser() : Execution Start");
        String url = CommonUtil.getUrl(request);
        Boolean registerUser = userService.registerUser(userDto, url);
        if (registerUser) {
            log.info("Error : {}","Register failed");
            return CommonUtil.createBuildResponseMessage("user created successfully! Please login:", HttpStatus.CREATED);
        } else {
            log.info("AuthController : registerUser() : Execution End");
            return CommonUtil.createErrorResponseMessage("user registration failed! Please try again:", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        LoginResponse loginResponse = userService.login(loginRequest);
        if (ObjectUtils.isEmpty(loginResponse)) {
            return CommonUtil.createBuildResponseMessage("login failed! Please login:", HttpStatus.BAD_REQUEST);
        }
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
        //return CommonUtil.createBuildResponseMessage("login successfully! Please login:", HttpStatus.OK);
    }

}
