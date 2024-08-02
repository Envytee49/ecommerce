package org.example.ecommerce.configuration.security;

import org.example.ecommerce.user.model.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public final class SecurityUtils {

    public static String getCurrentUserCartUuid() {
        return extractPrincipal(getAuthentication()).getClaim("uuidCart");
    }
    public static String getCurrentUserUuid() {
        return getAuthentication().getName();
    }
    private static Jwt extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return ((Jwt) authentication.getPrincipal());
    }
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
