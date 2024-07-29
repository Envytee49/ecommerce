package org.example.ecommerce.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.user.dto.request.CreateUserRequest;
import org.example.ecommerce.user.dto.request.UpdateUserRequest;
import org.example.ecommerce.user.dto.response.UserDetailResponse;
import org.example.ecommerce.user.dto.response.UserResponse;
import org.example.ecommerce.user.model.User;
import org.example.ecommerce.user.model.UserRole;
import org.example.ecommerce.user.repository.UserRepository;
import org.example.ecommerce.user.service.UserManagementService;
import org.example.ecommerce.voucher.repository.UserRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public PageDtoOut<UserResponse> getUsers(PageDtoIn pageDtoIn) {
        Pageable pageRequest = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize());
        Page<User> users = userRepository.findAll(pageRequest);
        // convert to product response
        List<UserResponse> userResponses = users
                .stream()
                .map(UserResponse::from)
                .toList();

        return PageDtoOut.from(
                pageDtoIn.getPage(),
                pageDtoIn.getSize(),
                users.getTotalElements(),
                userResponses);
    }

    @Override
    public void deleteUser(String userUuid) {
        User user = userRepository.findById(userUuid).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public void createUser(CreateUserRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        User user = User
                .builder()
                .registerDate(LocalDateTime.now())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobile(request.getMobile())
                .username(request.getUsername())
                .build();
        UserRole userRole = UserRole.builder()
                .uuidUser(user.getUuidUser())
                .role(request.getRole())
                .build();
        userRoleRepository.save(userRole);
        userRepository.save(user);
    }

    @Override
    public void updateUser(String uuidUser, UpdateUserRequest request) {
        User user = userRepository.findById(uuidUser)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setMobile(request.getMobile());
        user.setDescription(request.getDescription());
        user.setAvatar(request.getAvatar());
        userRepository.save(user);
    }

    @Override
    public UserDetailResponse getUserByUuid(String userUuid) {
        return null;
    }
}
