package org.example.ecommerce.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_user")
    private String uuidUser;

    @Id
    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_role")
    private String uuidRole;
}

