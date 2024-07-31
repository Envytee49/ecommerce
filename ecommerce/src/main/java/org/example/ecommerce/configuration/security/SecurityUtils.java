package org.example.ecommerce.configuration.security;

import org.example.ecommerce.user.model.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    public static String getCurrentUserCartUuid() {
        return extractPrincipal(getAuthentication()).getUuidCart();
    }
    public static String getCurrentUserUuid() {
        return extractPrincipal(getAuthentication()).getUuidUser();
    }
    private static SecurityUser extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return ((SecurityUser) authentication.getPrincipal());
    }
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
