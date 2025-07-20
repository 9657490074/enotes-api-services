package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.dto.PasswordChangeRequest;
import com.org.enotesapiservice.dto.UserResponse;
import com.org.enotesapiservice.entity.User;
import com.org.enotesapiservice.service.UserService;
import com.org.enotesapiservice.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        User loggedInUser = CommonUtil.getLoggedInUser();
        UserResponse userResponse = modelMapper.map(loggedInUser, UserResponse.class);
        return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordRequest) {
        userService.changePassword(passwordRequest);
        return CommonUtil.createBuildResponseMessage("password change successfully", HttpStatus.OK);
    }


}
