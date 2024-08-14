package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.common.dto.PageDtoOut;

import java.util.List;

@Getter
public class ProductReviewResponse extends PageDtoOut<ProductReviewDetailResponse> {
    private Double averageRating;

    public static ProductReviewResponse from(int page,
                                             int size,
                                             long totalElements,
                                             Double averageRating,
                                             List<ProductReviewDetailResponse> data) {
        long totalPages = totalElements / size;
        if (totalElements % size != 0) {
            totalPages++;
        }

        return ProductReviewResponse.reviewBuilder()
                .size(size)
                .page(page)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .averageRating(averageRating)
                .data(data)
                .build();

    }
    @Builder(builderMethodName = "reviewBuilder")
    public ProductReviewResponse(int page, int size, long totalPages, long totalElements, List<ProductReviewDetailResponse> data, Double averageRating) {
        super(page, size, totalPages, totalElements, data);
        this.averageRating = averageRating;
    }
}
