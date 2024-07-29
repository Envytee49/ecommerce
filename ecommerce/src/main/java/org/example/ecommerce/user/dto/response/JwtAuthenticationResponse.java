package org.example.ecommerce.user.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtAuthenticationResponse {
    private String token;
    private List<String> role;
}

