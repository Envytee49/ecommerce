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
import org.example.ecommerce.util.Utils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_address")
public class UserAddress {
    @Id
    @NotNull
    @Size(max = 40)
    @Builder.Default
    @Column(name = "uuid_address")
    private String uuidAddress = Utils.getUuid();

    @Size(max = 40)
    @Column(name = "uuid_user")
    private String uuidUser;

    @Size(max = 15)
    @Column(name = "mobile")
    private String mobile;

    @Size(max = 255)
    @Column(name = "city")
    private String city;

    @Size(max = 255)
    @Column(name = "street")
    private String street;

    @Size(max = 255)
    @Column(name = "district")
    private String district;

    @Column(name = "postal_code")
    private Integer postalCode;
}
