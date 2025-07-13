package com.org.enotesapiservice.service.impl;

import com.org.enotesapiservice.dto.UserDto;
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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Validation validation;
    private final ModelMapper modelMapper;

    @Override
    public Boolean registerUser(UserDto userDto) {
        validation.userValidation(userDto);
        User user = modelMapper.map(userDto, User.class);
        setRole(userDto, user);
        User savedUser = userRepository.save(user);
        if (!ObjectUtils.isEmpty(savedUser)) {
            return true;
        }
        return false;
    }

    private void setRole(UserDto userDto, User user) {
        //List<Integer> requestRole = userDto.getRoles().stream().map(UserDto.RoleDTO::getId).toList();
        List<Integer> requestRole = userDto.getRoles().stream().map(UserDto.RoleDTO::getId).toList();
        List<Role> roles = roleRepository.findAllById(requestRole);
        user.setRoles(roles);
    }
}
