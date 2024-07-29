package org.example.ecommerce.user.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.user.dto.response.JwtAuthenticationResponse;
import org.example.ecommerce.user.dto.request.SignInRequest;
import org.example.ecommerce.user.dto.request.SignUpRequest;
import org.example.ecommerce.user.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController extends AbstractController {
    private final AuthService authService;

    @PostMapping("/signUp")
    public ApiResponse<?> signup(@Valid @RequestBody SignUpRequest signUpRequest){
        return respond(() -> authService.signUp(signUpRequest), "Sign Up Successfully");
    }

    @PostMapping("/signIn")
    public ApiResponse<JwtAuthenticationResponse> signIn(@Valid @RequestBody SignInRequest signInRequest){
        return respond(() -> authService.signIn(signInRequest));
    }

}
