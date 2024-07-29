package org.example.ecommerce.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.user.dto.request.CreateUserRequest;
import org.example.ecommerce.user.dto.request.UpdateUserRequest;
import org.example.ecommerce.user.service.UserManagementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/management")
@RequiredArgsConstructor
public class UserManagementController extends AbstractController {
    private final UserManagementService userManagementService;
    @GetMapping
    public ApiResponse<?> getUsers(@Valid @RequestBody PageDtoIn pageDtoIn) {
        return respond(() -> userManagementService.getUsers(pageDtoIn));
    }
    @GetMapping("/{uuidUser}")
    public ApiResponse<?> getUserByUuid(@PathVariable String uuidUser) {
        return respond(() -> userManagementService.getUserByUuid(uuidUser));
    }
    @PostMapping
    public ApiResponse<?> addUser(@Valid @RequestBody CreateUserRequest request) {
        return respond(() -> userManagementService.createUser(request), "User created");
    }
    @PutMapping("/{uuidUser}")
    public ApiResponse<?> updateUser(@PathVariable String uuidUser,@Valid @RequestBody UpdateUserRequest request) {
        return respond(() -> userManagementService.updateUser(uuidUser, request), "User updated");
    }
    @DeleteMapping("/{uuidUser}")
    public ApiResponse<?> deleteUser(@PathVariable String uuidUser) {
        return respond(() -> userManagementService.deleteUser(uuidUser), "User deleted");
    }
}
