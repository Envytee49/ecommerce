package org.example.ecommerce.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.util.Utils;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sku")
public class Sku {
    @Id
    @Size(max = 36)
    @Builder.Default
    @Column(name = "uuid_sku")
    private String uuidSku = Utils.getUuid();
    @Column(name = "uuid_product")
    @Size(max = 40)
    private String uuidProduct;
    @Column(name = "sku")
    private String sku;
    @Column(name = "price")
    private double price;
    @Column(name = "quantity")
    private int quantity;

    @Override
    public String toString() {
        return "Sku{" +
                "uuidSku='" + uuidSku + '\'' +
                ", uuidProduct='" + uuidProduct + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
