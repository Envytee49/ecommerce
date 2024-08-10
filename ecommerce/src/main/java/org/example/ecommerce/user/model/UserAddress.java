package org.example.ecommerce.user.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.common.util.Utils;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_address")
public class UserAddress extends AbstractAddress{
    @Id
    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_uaddress")
    private String uuidUAddress = Utils.getUuid();

    @Size(max = 40)
    @Column(name = "uuid_user")
    private String uuidUser;

    @Size(max = 30)
    @Column(name = "receiver_name")
    private String receiverName;
}
