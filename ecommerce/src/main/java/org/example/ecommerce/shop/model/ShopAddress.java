package org.example.ecommerce.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.common.util.Utils;
import org.example.ecommerce.user.model.AbstractAddress;

@Getter
@Setter
@Entity
@Table(name = "shop_address")
public class ShopAddress extends AbstractAddress {
    @Id
    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_saddress")
    private String uuidSAddress = Utils.getUuid();

    @Size(max = 40)
    @Column(name = "uuid_shop")
    private String uuidShop;

    @Size(max = 30)
    @Column(name = "seller_name")
    private String sellerName;
}