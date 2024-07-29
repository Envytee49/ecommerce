package org.example.ecommerce.user.service;

import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.user.dto.request.CreateUserRequest;
import org.example.ecommerce.user.dto.request.UpdateUserRequest;
import org.example.ecommerce.user.dto.response.UserDetailResponse;
import org.example.ecommerce.user.dto.response.UserResponse;

public interface UserManagementService {
    PageDtoOut<UserResponse> getUsers(PageDtoIn pageDtoIn);
    void deleteUser(String userUuid);
    void createUser(CreateUserRequest request);
    void updateUser(String uuidUser, UpdateUserRequest request);
    UserDetailResponse getUserByUuid(String userUuid);
}
