package com.org.enotesapiservice.service.impl;

import com.org.enotesapiservice.dto.EmailRequest;
import com.org.enotesapiservice.dto.UserDto;
import com.org.enotesapiservice.entity.AccountStatus;
import com.org.enotesapiservice.entity.Role;
import com.org.enotesapiservice.entity.User;
import com.org.enotesapiservice.repository.RoleRepository;
import com.org.enotesapiservice.repository.UserRepository;
import com.org.enotesapiservice.service.UserService;
import com.org.enotesapiservice.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Validation validation;
    private final ModelMapper modelMapper;
    private final EmailSendService emailSendService;

    @Override
    public Boolean registerUser(UserDto userDto,String url) throws Exception {
        validation.userValidation(userDto);
        User user = modelMapper.map(userDto, User.class);
        setRole(userDto, user);
        AccountStatus status= AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);
        User savedUser = userRepository.save(user);
        if (!ObjectUtils.isEmpty(savedUser)) {
            emailSend(savedUser,url);
            return true;
        }
        return false;
    }

    private void emailSend(User savedUser,String url) throws Exception {
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

    private void setRole(UserDto userDto, User user) {
        //List<Integer> requestRole = userDto.getRoles().stream().map(UserDto.RoleDTO::getId).toList();
        List<Integer> requestRole = userDto
                .getRoles()
                .stream()
                .map(UserDto.RoleDTO::getId)
                .toList();
        List<Role> roles = roleRepository.findAllById(requestRole);
        user.setRoles(roles);
    }
}
