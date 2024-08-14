package org.example.ecommerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.Role;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Email must not be blank")
    private String email;
    @NotBlank(message = "password must not be blank")
    private String password;
    private String username;
    private String mobile;
    @NotNull
    private Role role;

}
