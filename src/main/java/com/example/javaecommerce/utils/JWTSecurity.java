package com.example.javaecommerce.utils;

import com.example.javaecommerce.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public final class JWTSecurity {
    public static Optional<UserDetailsImpl> getJWTUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() != null) {
            Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return object instanceof UserDetailsImpl ? Optional.of((UserDetailsImpl) object) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }
    public static String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://" + httpServletRequest.getServerName()
                + ":" + httpServletRequest.getServerPort()
                + httpServletRequest.getContextPath();
    }
}
