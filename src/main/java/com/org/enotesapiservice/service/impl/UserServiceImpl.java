package com.org.enotesapiservice.service.impl;

import com.org.enotesapiservice.dto.PasswordChangeRequest;
import com.org.enotesapiservice.entity.User;
import com.org.enotesapiservice.repository.UserRepository;
import com.org.enotesapiservice.service.UserService;
import com.org.enotesapiservice.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void changePassword(PasswordChangeRequest passwordRequest) {
        User loggedInUser = CommonUtil.getLoggedInUser();
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), loggedInUser.getPassword())) {
            throw new IllegalArgumentException("old password is in-correct");
        }
        String encode = passwordEncoder.encode(passwordRequest.getNewPassword());
        loggedInUser.setPassword(encode);
        userRepository.save(loggedInUser);
    }
}
