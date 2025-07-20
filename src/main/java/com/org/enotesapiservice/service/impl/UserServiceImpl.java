package com.org.enotesapiservice.service.impl;

import com.org.enotesapiservice.dto.EmailRequest;
import com.org.enotesapiservice.dto.PasswordChangeRequest;
import com.org.enotesapiservice.dto.PasswordResetRequest;
import com.org.enotesapiservice.entity.User;
import com.org.enotesapiservice.exception.ResourceNotFoundException;
import com.org.enotesapiservice.repository.UserRepository;
import com.org.enotesapiservice.service.UserService;
import com.org.enotesapiservice.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailSendService emailSendService;

    @Override
    public void changePassword(PasswordChangeRequest passwordRequest) {
        User loggedInUser = CommonUtil.getLoggedInUser();
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), loggedInUser.getPassword())) {
            throw new IllegalArgumentException("old password is in-correct");
        }
        String encode = passwordEncoder.encode(passwordRequest.getNewPassword());
        loggedInUser.setPassword(encode);
        userRepository.save(loggedInUser);
    }

    @Override
    public void sendEmailPasswordReset(String email,
                                       HttpServletRequest request) throws Exception {
        User user = userRepository.findByEmail(email);
        if (ObjectUtils.isEmpty(user)) {
            throw new ResourceNotFoundException("email-id is not exist");
        }
        // Generate unique password reset token
        String passwordResetToken = UUID.randomUUID().toString();
        user.getStatus().setPasswordResetToken(passwordResetToken);
        User updateUser = userRepository.save(user);

        String url = CommonUtil.getUrl(request);
        sendEmailRequest(updateUser, url);
    }

    @Override
    public void verifyPasswordResetLink(Integer uid,
                                        String code) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user"));
        verifyPasswordResetToken(user.getStatus().getPasswordResetToken(), code);
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        User user = userRepository.findById(passwordResetRequest.getUid())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user"));
        String encodePassword = passwordEncoder.encode(passwordResetRequest.getNewPassword());
        user.setPassword(encodePassword);
        user.getStatus().setPasswordResetToken(null);
        userRepository.save(user);
    }

    private void verifyPasswordResetToken(String existToken,
                                          String requestToken) {
        if (StringUtils.hasText(requestToken)) {
            if (!StringUtils.hasText(existToken)) {
                throw new IllegalArgumentException("already password reset");
            }
            if (!existToken.equals(requestToken)) {
                throw new IllegalArgumentException("invalid-token");
            }

        } else {
            throw new IllegalArgumentException("invalid-token");
        }

    }

    private void sendEmailRequest(User user, String url) throws Exception {
        String message = "Hi <b>[[username]]</b> "
                + "<br><p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=[[url]]>Change my password</a></p>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p><br>"
                + "Thanks,<br>E-notes.com";

        message = message.replace("[[username]]", user.getFirstName());
        message = message.replace("[[url]]", url + "/api/v1/home/verify-pswd-link?uid=" + user.getId() + "&&code="
                + user.getStatus().getPasswordResetToken());

        EmailRequest emailRequest = EmailRequest.builder().to(user.getEmail())
                .title("Password Reset").subject("Password Reset link").message(message).build();

        // send password reset email to user
        emailSendService.send(emailRequest);
    }
}
