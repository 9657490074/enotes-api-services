package com.org.enotesapiservice.util;

import com.org.enotesapiservice.dto.CategoryDto;
import com.org.enotesapiservice.dto.TodoDto;
import com.org.enotesapiservice.dto.UserDto;
import com.org.enotesapiservice.entity.Role;
import com.org.enotesapiservice.enums.ToDoStatus;
import com.org.enotesapiservice.exception.ExistDataException;
import com.org.enotesapiservice.exception.ResourceNotFoundException;
import com.org.enotesapiservice.exception.ValidationException;
import com.org.enotesapiservice.repository.RoleRepository;
import com.org.enotesapiservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Validation {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void categoryValidation(CategoryDto categoryDto) {

        Map<String, Object> error = new LinkedHashMap<>();

        if (ObjectUtils.isEmpty(categoryDto)) {
            throw new IllegalArgumentException("category Object/JSON shouldn't be null or empty");
        } else {

            // validation name field
            if (ObjectUtils.isEmpty(categoryDto.getName())) {
                error.put("name", "name field is empty or null");
            } else {
                if (categoryDto.getName().length() < 3) {
                    error.put("name", "name length min 3");
                }
                if (categoryDto.getName().length() > 100) {
                    error.put("name", "name length max 100");
                }
            }

            // validation dscription
            if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
                error.put("description", "description field is empty or null");
            }

            // validation isActive
            if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
                error.put("isActive", "isActive field is empty or null");
            } else {
                if (categoryDto.getIsActive() != Boolean.TRUE
                        && categoryDto.getIsActive() != Boolean.FALSE) {
                    error.put("isActive", "invalid value isActive field ");
                }
            }
        }
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }

    }

    public void todoValidation(TodoDto todo) throws Exception {

        TodoDto.StatusDto reqStatus = todo.getStatus();
        boolean statusFound = false;

        for (ToDoStatus st : ToDoStatus.values()) {
            if (st.getId().equals(reqStatus.getId())) {
                statusFound = true;
            }
        }
        if (!statusFound) {
            throw new ResourceNotFoundException("invalid status");
        }
    }

    public void userValidation(UserDto userDto) {

        if (!StringUtils.hasText(userDto.getFirstName())) {
            throw new IllegalArgumentException("first name is invalid");
        }

        if (!StringUtils.hasText(userDto.getLastName())) {
            throw new IllegalArgumentException("last name is invalid");
        }

        if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constant.EMAIL_REGEX)) {
            throw new IllegalArgumentException("email is invalid");
        } else {
            // validate email exist
            Boolean existEmail = userRepository.existsByEmail(userDto.getEmail());
            if (existEmail) {
                throw new ExistDataException("Email already exist");
            }
        }

        if (!StringUtils.hasText(userDto.getMobileNo()) || !userDto.getMobileNo().matches(Constant.MOBILE_NO_REGEX)) {
            throw new IllegalArgumentException("mobile number is invalid");
        }

        if (CollectionUtils.isEmpty(userDto.getRoles())) {
            throw new IllegalArgumentException("role is invalid");
        } else {

            //List<Integer> roleIds = roleRepository.findAll().stream().map(r -> r.getId()).toList();
            List<Integer> roleIds = roleRepository.findAll()
                    .stream()
                    .map(Role::getId)
                    .toList();

            List<Integer> invalidReqRoleIds = userDto
                    .getRoles()
                    .stream()
                    .map(UserDto.RoleDTO::getId)
                    .filter(roleId -> !roleIds.contains(roleId))
                    .toList();

            if (!CollectionUtils.isEmpty(invalidReqRoleIds)) {
                throw new IllegalArgumentException("role is invalid" + invalidReqRoleIds);
            }
        }

    }
}
