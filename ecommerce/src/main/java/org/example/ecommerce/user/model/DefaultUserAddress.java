package org.example.ecommerce.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.ecommerce.common.util.Utils;
@Entity
@Table(name = "default_user_address")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultUserAddress{
    @Id
    @Size(max = 40)
    @Column(name = "uuid_user")
    private String uuidUser;
    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_uaddress")
    private String uuidUAddress = Utils.getUuid();
}
