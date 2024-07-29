package org.example.ecommerce.user.model;

import lombok.Getter;
import org.example.ecommerce.common.constants.Role;

import java.io.Serializable;
@Getter
public class UserRoleId implements Serializable {
    private String uuidUser;
    private Role role;
}
