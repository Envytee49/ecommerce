package org.example.ecommerce.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.model.AbstractEntity;

@Table(name = "attribute")
@Getter
@Setter

public class Attribute extends AbstractEntity {
    @Column(name = "uuid_attribute")
    @Size(max = 40)
    private String uuidAttribute;
    @Column(name = "key")
    @Size(max = 40)
    private String key;

}
