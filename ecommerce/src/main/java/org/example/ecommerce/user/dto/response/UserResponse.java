package org.example.ecommerce.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.Role;
import org.example.ecommerce.user.model.User;

import java.util.List;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String uuidUser;
    private String uuidCart;
    private String email;
    private String username;
    private String mobile;
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .uuidUser(user.getUuidUser())
                .uuidCart(user.getUuidCart())
                .email(user.getEmail())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .build();
    }
}
