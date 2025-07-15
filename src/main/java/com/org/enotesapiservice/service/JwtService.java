package com.org.enotesapiservice.service;

import com.org.enotesapiservice.entity.User;

public interface JwtService {

    String generateToken(User user);
}
