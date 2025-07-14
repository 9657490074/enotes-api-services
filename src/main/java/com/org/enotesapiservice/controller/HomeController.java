package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.exception.SuccessException;
import com.org.enotesapiservice.service.HomeService;
import com.org.enotesapiservice.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
public class HomeController {

    private final HomeService homeService;

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
}
