package org.example.ecommerce.configuration.filter;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String extractUserName(String token);
    boolean isValidToken(String token, UserDetails userDetails);
}
