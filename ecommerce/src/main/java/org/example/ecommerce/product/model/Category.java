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
import org.example.ecommerce.common.util.Utils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Size(max = 40)
    @Builder.Default
    @Column(name = "uuid_category")
    private String uuidCategory = Utils.getUuid();

    @NotNull
    @Size(max = 75)
    @Column(name = "title")
    private String title;

    @Builder.Default
    @Size(max = 100)
    @Column(name = "meta_title")
    private String metaTitle = null;

    @NotNull
    @Size(max = 100)
    @Column(name = "slug")
    private String slug;

    @Column(name = "content")
    private String content;
}
