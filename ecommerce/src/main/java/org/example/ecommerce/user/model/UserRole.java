package org.example.ecommerce.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.ecommerce.common.constants.Role;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(UserRoleId.class)
@Table(name = "user_role")
public class UserRole {
    @Id
    @Column(name = "uuid_user")
    @Size(max = 40)
    private String uuidUser;
    @Id
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
