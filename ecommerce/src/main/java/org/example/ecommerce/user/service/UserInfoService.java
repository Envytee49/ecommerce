package org.example.ecommerce.user.service;

import org.example.ecommerce.user.dto.request.ChangePasswordRequest;
import org.example.ecommerce.user.dto.request.UpdateProfileRequest;
import org.example.ecommerce.user.dto.response.UserAddressResponse;

public interface UserInfoService {
    UserAddressResponse getUserAddress();
    void changePassword(ChangePasswordRequest request);
    void updateProfile(UpdateProfileRequest request);
    void changeDefaultAddress(String uuidUAddress);
}
