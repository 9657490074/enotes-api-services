package com.org.enotesapiservice.service;

import com.org.enotesapiservice.dto.PasswordChangeRequest;
import com.org.enotesapiservice.dto.PasswordResetRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    void changePassword(PasswordChangeRequest passwordRequest);

    void sendEmailPasswordReset(String email,
                                HttpServletRequest request)
            throws Exception;

    void verifyPasswordResetLink(Integer uid, String code);

    void resetPassword(PasswordResetRequest passwordResetRequest);
}
