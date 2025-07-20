package com.org.enotesapiservice.service;

import com.org.enotesapiservice.exception.SuccessException;

public interface HomeService {
    Boolean verifyUserAccount(Integer userId,
                              String verificationCode) throws SuccessException;
}
