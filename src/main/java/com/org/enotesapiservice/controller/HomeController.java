package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.dto.PasswordResetRequest;
import com.org.enotesapiservice.exception.SuccessException;
import com.org.enotesapiservice.service.HomeService;
import com.org.enotesapiservice.service.UserService;
import com.org.enotesapiservice.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
public class HomeController {

    private final HomeService homeService;
    private final UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid,
                                               @RequestParam String code) throws SuccessException {
        Boolean verifyUser = homeService.verifyUserAccount(uid, code);
        if (verifyUser) {
            return CommonUtil.createBuildResponseMessage("User account verified", HttpStatus.OK);
        } else {
            return CommonUtil.createBuildResponseMessage("User account not verified", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/send-email-reset")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email,
                                                       HttpServletRequest request) throws Exception {
        userService.sendEmailPasswordReset(email, request);
        return CommonUtil.createBuildResponseMessage("email send success ! please reset password", HttpStatus.OK);
    }

    @GetMapping("/verify-password-link")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid,
                                                     @RequestParam String code) {
        userService.verifyPasswordResetLink(uid,code);
        return CommonUtil.createBuildResponseMessage("verified success",HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        userService.resetPassword(passwordResetRequest);
        return CommonUtil.createBuildResponseMessage("password-reset-successfully",HttpStatus.OK);
    }
}
