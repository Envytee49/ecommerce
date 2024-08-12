package org.example.ecommerce.product.model;


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
import org.example.ecommerce.common.model.AbstractEntity;
import org.example.ecommerce.common.util.Utils;

@Entity
@Table(name = "product_review")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview extends AbstractEntity {
    @Id
    @NotNull
    @Size(max = 40)
    @Builder.Default
    @Column(name = "uuid_product_review")
    private String uuidProductReview = Utils.getUuid();

    @Size(max = 40)
    @Column(name = "uuid_product")
    private String uuidProduct;

    @Size(max = 40)
    @Column(name = "uuid_parent_product_review")
    private String uuidParentProductReview;

    @Size(max = 40)
    @Column(name = "uuid_user")
    private String uuidUser;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @Size(max = 100)
    @Column(name = "title")
    private String title;

    @NotNull
    @Builder.Default
    @Column(name = "rating")
    private int rating = 0;

}

