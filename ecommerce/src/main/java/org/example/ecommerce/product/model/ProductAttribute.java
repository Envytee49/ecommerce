//package org.example.ecommerce.product.model;
//
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.example.ecommerce.common.util.Utils;
//
//@Entity
//@Table(name = "product_attribute")
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class ProductAttribute {
//    @Id
//    @Builder.Default
//    @Column(name = "uuid_product_attribute")
//    private String uuidProductAttribute = Utils.getUuid();
//    @NotNull
//    @Column(name = "uuid_product")
//    private String uuidProduct;
//    @NotNull
//    @Column(name = "uuid_attribute")
//    private String uuidAttribute;
//
//    @Size(max = 200)
//    @Column(name = "_value")
//    private String value;
//}
//
