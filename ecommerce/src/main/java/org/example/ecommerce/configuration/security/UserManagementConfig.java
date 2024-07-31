package org.example.ecommerce.configuration.security;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.user.model.SecurityUser;
import org.example.ecommerce.user.model.User;
import org.example.ecommerce.user.repository.UserRepository;
import org.example.ecommerce.voucher.repository.UserRoleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UserManagementConfig {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository
                    .findByEmail(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            List<String> roles = userRoleRepository.findByUuidUser(user.getUuidUser())
                    .stream().map(userRole -> userRole.getRole().name()).toList();
            List<SimpleGrantedAuthority> authorities =
                    roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
            return  new SecurityUser(
                    user.getEmail(),
                    user.getPassword(),
                    authorities,
                    user.getUuidUser(),
                    user.getUuidCart());
        };
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
