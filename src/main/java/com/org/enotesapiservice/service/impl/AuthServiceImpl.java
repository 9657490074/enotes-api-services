package com.org.enotesapiservice.service.impl;

import com.org.enotesapiservice.config.security.CustomUserDetails;
import com.org.enotesapiservice.dto.*;
import com.org.enotesapiservice.entity.AccountStatus;
import com.org.enotesapiservice.entity.Role;
import com.org.enotesapiservice.entity.User;
import com.org.enotesapiservice.repository.RoleRepository;
import com.org.enotesapiservice.repository.UserRepository;
import com.org.enotesapiservice.service.JwtService;
import com.org.enotesapiservice.service.AuthService;
import com.org.enotesapiservice.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Validation validation;
    private final ModelMapper modelMapper;
    private final EmailSendService emailSendService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public Boolean registerUser(UserRequest userDto, String url) throws Exception {
        validation.userValidation(userDto);
        User user = modelMapper.map(userDto, User.class);
        setRole(userDto, user);
        AccountStatus status = AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        if (!ObjectUtils.isEmpty(savedUser)) {
            emailSendForRegister(savedUser, url);
            return true;
        }
        return false;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
            String token = jwtService.generateToken(customUserDetails.getUser());
            return LoginResponse.builder()
                    .user(modelMapper.map(customUserDetails.getUser(), UserResponse.class))
                    .token(token)
                    .build();
        }
        return null;
    }

    private void emailSendForRegister(User savedUser, String url) throws Exception {
        String message = "Hi,<b>[[username]]</b> " + "<br> Your account register sucessfully.<br>"
                + "<br> Click the below link verify & Active your account <br>"
                + "<a href='[[url]]'>Click Here</a> <br><br>" + "Thanks,<br>Enotes.com";

        message = message.replace("[[username]]", savedUser.getFirstName());
        message = message.replace("[[url]]", url + "/api/v1/home/verify?uid=" + savedUser.getId() + "&&code="
                + savedUser.getStatus().getVerificationCode());

        EmailRequest emailRequest = EmailRequest.builder().to(savedUser.getEmail())
                .title("Account Creating Confirmation").subject("Account Created Success").message(message).build();
        emailSendService.send(emailRequest);
    }

    private void setRole(UserRequest userDto, User user) {
        //List<Integer> requestRole = userDto.getRoles().stream().map(UserDto.RoleDTO::getId).toList();
        List<Integer> requestRole = userDto
                .getRoles()
                .stream()
                .map(UserRequest.RoleDTO::getId)
                .toList();
        List<Role> roles = roleRepository.findAllById(requestRole);
        user.setRoles(roles);
    }
}
