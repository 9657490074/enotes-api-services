package com.org.enotesapiservice.service.impl;

import com.org.enotesapiservice.entity.AccountStatus;
import com.org.enotesapiservice.entity.User;
import com.org.enotesapiservice.exception.ResourceNotFoundException;
import com.org.enotesapiservice.exception.SuccessException;
import com.org.enotesapiservice.repository.UserRepository;
import com.org.enotesapiservice.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeServiceImpl implements HomeService {

    private final UserRepository userRepository;

    @Override
    public Boolean verifyUserAccount(Integer userId, String verificationCode) throws SuccessException {
        log.info("HomeServiceImpl : verifyAccount() : Start");
        User userFound = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userFound.getStatus().getVerificationCode()==null){
            log.info("message : Account already verified");
            throw new SuccessException("Account already verified:");
        }

        if (userFound.getStatus().getVerificationCode().equals(verificationCode)) {
            AccountStatus status = userFound.getStatus();
            status.setIsActive(true);
            status.setVerificationCode(null);
            userRepository.save(userFound);
            log.info("message : Account verification success");
            return true;
        }
        log.info("HomeServiceImpl : verifyAccount() : End");
        return false;
    }
}
