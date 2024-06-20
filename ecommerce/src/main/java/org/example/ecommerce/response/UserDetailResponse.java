package org.example.ecommerce.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetailResponse {
    private int userId;
    private String firstName;
    private String lastName;
    private int age;
}
