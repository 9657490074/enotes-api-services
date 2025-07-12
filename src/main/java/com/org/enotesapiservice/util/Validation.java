package com.org.enotesapiservice.util;

import com.org.enotesapiservice.dto.CategoryDto;
import com.org.enotesapiservice.dto.TodoDto;
import com.org.enotesapiservice.enums.ToDoStatus;
import com.org.enotesapiservice.exception.ResourceNotFoundException;
import com.org.enotesapiservice.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Validation {

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
}
