package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.dto.UserDto;
import com.org.enotesapiservice.service.UserService;
import com.org.enotesapiservice.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws Exception {
        Boolean registerUser = userService.registerUser(userDto);
        if (registerUser) {
            return CommonUtil.createBuildResponseMessage("user created successfully! Please login:", HttpStatus.CREATED);
        } else {
            return CommonUtil.createErrorResponseMessage("user registration failed! Please try again:", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
