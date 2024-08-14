package org.example.ecommerce.product.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.example.ecommerce.common.constants.sortby.SPEnum;
import org.example.ecommerce.order.model.OrderItem;
import org.example.ecommerce.product.dto.response.ProductResponse;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.model.ProductReview;
import org.example.ecommerce.product.projections.ProductResponseProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SearchProductRepositoryImpl implements SearchProductRepository {
    private static final Logger log = LoggerFactory.getLogger(SearchProductRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ProductResponse> findAllWithFilters(
            String keyword, String sortBy, String sortDirection,
            Double minPrice, Double maxPrice, Integer ratingFilter,
            Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Product> root = query.from(Product.class);

        // Joins
        Join<Product, ProductReview> productReviewJoin = root.join("productReviews", JoinType.LEFT);
        Join<Product, OrderItem> orderItemJoin = root.join("orderItems", JoinType.LEFT);

        // Filters
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasLength(keyword)) {
            predicates.add(cb.like(root.get("title"), "%" + keyword + "%"));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        // Group by uuidProduct
        query.groupBy(root.get("uuidProduct"));

        // Having filter for rating
        if (ratingFilter != null) {
            query.having(cb.greaterThanOrEqualTo(cb.avg(productReviewJoin.get("rating")), ratingFilter * 1.0));
        }

        // Sorting
        if (StringUtils.hasLength(sortBy)) {
            String orderBy = SPEnum.valueOf(sortBy).getAlias();
            if ("sale".equals(orderBy)) {
                query.orderBy(cb.desc(cb.sum(orderItemJoin.get("quantity"))));
            } else {
                if ("desc".equalsIgnoreCase(sortDirection)) {
                    query.orderBy(cb.desc(root.get(orderBy)));
                } else {
                    query.orderBy(cb.asc(root.get(orderBy)));
                }
            }
        }

        // Projections
        query.multiselect(
                root.get("uuidProduct").alias("uuidProduct"),
                root.get("title").alias("title"),
                root.get("price").alias("price"),
                root.get("discount").alias("discount"),
                cb.avg(productReviewJoin.get("rating")).alias("averageRating"),
                cb.sumAsLong(orderItemJoin.get("quantity")).alias("sold")
        );

        query.where(predicates.toArray(new Predicate[0]));

        // Create the typed query
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);

        // Apply pagination
        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        // Get the results
        List<Object[]> results = typedQuery.getResultList();

        List<ProductResponse> productResponses = new ArrayList<>();
        for (Object[] result : results) {
            productResponses.add(new ProductResponse(
                    (String) result[0],
                    (String) result[1],
                    (Double) result[2],
                    (Double) result[3],
                    (Double) result[4],
                    (Double) result[5]));
        }
        // Return as Page
        return new PageImpl<>(productResponses, pageable, totalRows);
    }
}
