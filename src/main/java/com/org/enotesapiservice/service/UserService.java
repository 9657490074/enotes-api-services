package com.org.enotesapiservice.service;

import com.org.enotesapiservice.dto.PasswordChangeRequest;

public interface UserService {

    void changePassword(PasswordChangeRequest passwordRequest);
}
