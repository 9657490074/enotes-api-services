package com.org.enotesapiservice.service;

import com.org.enotesapiservice.dto.LoginRequest;
import com.org.enotesapiservice.dto.LoginResponse;
import com.org.enotesapiservice.dto.UserDto;

public interface UserService {

    Boolean registerUser(UserDto userDto,String url) throws Exception;

    LoginResponse login(LoginRequest loginRequest);
}
