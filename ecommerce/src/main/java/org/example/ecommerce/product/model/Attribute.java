//package org.example.ecommerce.product.model;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.Size;
//import lombok.*;
//import org.example.ecommerce.common.model.AbstractEntity;
//import org.example.ecommerce.common.util.Utils;
//
//@Table(name = "attribute")
//@Getter
//@Setter
//@Entity
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class Attribute extends AbstractEntity {
//    @Column(name = "uuid_attribute")
//    @Size(max = 40)
//    @Id
//    @Builder.Default
//    private String uuidAttribute = Utils.getUuid();
//    @Column(name = "_key")
//    @Size(max = 40)
//    private String key;
//
//}
