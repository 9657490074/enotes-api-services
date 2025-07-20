package com.org.enotesapiservice.config;

import com.org.enotesapiservice.entity.User;
import com.org.enotesapiservice.util.CommonUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareConfig implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        User loggedInUser = CommonUtil.getLoggedInUser();
        return Optional.of(loggedInUser.getId());
    }
}
