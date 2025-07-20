package com.org.enotesapiservice.service;

import com.org.enotesapiservice.dto.LoginRequest;
import com.org.enotesapiservice.dto.LoginResponse;
import com.org.enotesapiservice.dto.UserRequest;

public interface AuthService {

    Boolean registerUser(UserRequest userDto, String url) throws Exception;

    LoginResponse login(LoginRequest loginRequest);

}
