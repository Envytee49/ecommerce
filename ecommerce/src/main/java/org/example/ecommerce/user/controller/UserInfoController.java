package org.example.ecommerce.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.user.dto.request.ChangePasswordRequest;
import org.example.ecommerce.user.dto.request.UpdateProfileRequest;
import org.example.ecommerce.user.dto.request.UpdateUserRequest;
import org.example.ecommerce.user.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/info")
@RequiredArgsConstructor
public class UserInfoController extends AbstractController {
    private final UserInfoService userInfoService;
    @GetMapping("/addresses")
    public ApiResponse<?> getUserAddress() {
        return respond(() -> userInfoService.getUserAddress());
    }
    @PutMapping("/profile")
    public ApiResponse<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return respond(() -> userInfoService.updateProfile(request), "Profile updated");
    }
    @PatchMapping
    public ApiResponse<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return respond(() -> userInfoService.changePassword(request), "Password Changed");
    }
}
