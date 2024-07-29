package org.example.ecommerce.user.service;

import org.example.ecommerce.user.dto.response.JwtAuthenticationResponse;
import org.example.ecommerce.user.dto.request.SignInRequest;
import org.example.ecommerce.user.dto.request.SignUpRequest;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signIn(SignInRequest signInRequest);
    void logOut();
}
