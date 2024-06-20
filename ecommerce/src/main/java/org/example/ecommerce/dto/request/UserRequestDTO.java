package org.example.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "first name must not be blank")
    private String firstName;
    @NotBlank(message = "last name must not be blank")
    private String lastName;
    @Email(message = "Invalid email format")
    private String email;
    private String phone;
}
