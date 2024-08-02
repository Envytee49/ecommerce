package org.example.ecommerce.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.user.dto.response.JwtAuthenticationResponse;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.user.dto.request.SignInRequest;
import org.example.ecommerce.user.dto.request.SignUpRequest;
import org.example.ecommerce.user.model.User;
import org.example.ecommerce.user.repository.UserRepository;
import org.example.ecommerce.user.service.AuthService;
import org.example.ecommerce.configuration.jwt.JwtService;
import org.example.ecommerce.voucher.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRoleRepository userRoleRepository;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .registerDate(LocalDateTime.now())
                .build();
        userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {

        User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        List<String> roles = userRoleRepository.findByUuidUser(user.getUuidUser())
                .stream().map(userRole -> userRole.getRole().name()).toList();

        String jwt = jwtService.generateToken(user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRole(roles);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return jwtAuthenticationResponse;
    }

    @Override
    public void logOut() {

    }
}
