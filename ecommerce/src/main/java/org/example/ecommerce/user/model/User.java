package org.example.ecommerce.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.ecommerce.common.util.Utils;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @NotNull
    @Column(name = "uuid_user")
    @Builder.Default
    @Size(max = 40)
    private String uuidUser = Utils.getUuid();

    @NotNull
    @Column(name = "uuid_cart")
    @Size(max = 40)
    @Builder.Default
    private String uuidCart = Utils.getUuid();

    @Column(name = "first_name")
    @Size(max = 50)
    @Builder.Default
    private String firstName = null;

    @Column(name = "middle_name")
    @Size(max = 50)
    @Builder.Default
    private String middleName = null;

    @Column(name = "last_name")
    @Size(max = 50)
    @Builder.Default
    private String lastName = null;

    @Column(name = "mobile")
    @Size(max = 15)
    @Builder.Default
    private String mobile = null;

    @Column(name = "email")
    @Size(max = 50)
    @Builder.Default
    private String email = null;


    @Column(name = "avatar")
    @Size(max = 200)
    @Builder.Default
    private String avatar = null;

    @Column(name = "description", columnDefinition = "TEXT")
    @Builder.Default
    private String description = null;

    @NotNull
    @Column(name = "password")
    @Size(max = 32 )
    private String password;

    @NotNull
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @Builder.Default
    @Column(name = "last_login")
    private LocalDateTime lastLogin = null;

    @Builder.Default
    @Column(name = "activate")
    private int activate = 0;

    @PrePersist
    void prePersist() {
        registerDate = LocalDateTime.now();
    }

}
