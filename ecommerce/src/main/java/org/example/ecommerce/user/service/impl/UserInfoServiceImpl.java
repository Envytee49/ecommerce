package org.example.ecommerce.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.user.dto.request.ChangePasswordRequest;
import org.example.ecommerce.user.dto.request.UpdateProfileRequest;
import org.example.ecommerce.user.dto.response.UserAddressDetail;
import org.example.ecommerce.user.dto.response.UserAddressResponse;
import org.example.ecommerce.user.model.User;
import org.example.ecommerce.user.model.UserAddress;
import org.example.ecommerce.user.repository.UserAddressRepository;
import org.example.ecommerce.user.repository.UserRepository;
import org.example.ecommerce.user.service.UserInfoService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updateProfile(UpdateProfileRequest request) {
        User user = userRepository.findById(SecurityUtils.getCurrentUserUuid())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setMobile(request.getMobile());
        user.setDescription(request.getDescription());
        user.setAvatar(request.getAvatar());
        userRepository.save(user);
    }

    @Override
    public UserAddressResponse getUserAddress() {
        List<UserAddress> userAddresses =
                userAddressRepository.findByUuidUser(SecurityUtils.getCurrentUserUuid());
        List<UserAddressDetail> userAddressResponse =
                userAddresses.stream().map(UserAddressDetail::from).toList();
        return UserAddressResponse.builder()
                .userAddresses(userAddressResponse)
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();
        User user = userRepository.findById(SecurityUtils.getCurrentUserUuid())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new AppException(ErrorCode.WRONG_OLD_PASSWORD);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
