//package org.example.ecommerce.product.repository.specification;
//
//import jakarta.persistence.criteria.Join;
//import jakarta.persistence.criteria.JoinType;
//import jakarta.persistence.criteria.Predicate;
//import org.example.ecommerce.common.constants.sortby.SPEnum;
//import org.example.ecommerce.order.model.OrderItem;
//import org.example.ecommerce.product.model.Product;
//import org.example.ecommerce.product.model.ProductReview;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProductSpecifications {
//
//    private static final Logger log = LoggerFactory.getLogger(ProductSpecifications.class);
//
//    public static Specification<Product> withFilters(String keyword, String sortBy, String sortDirection,
//                                                     Double minPrice, Double maxPrice, Integer ratingFilter) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            // Keyword filter
//            if (StringUtils.hasLength(keyword)) {
//                predicates.add(criteriaBuilder.like(root.get("title"), "%" + keyword + "%"));
//            }
//
//            // Price filter
//            if (minPrice != null) {
//                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
//            }
//            if (maxPrice != null) {
//                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
//            }
//
//            // Rating filter
//            if (ratingFilter != null) {
//                Join<Product,ProductReview> productReview = root.join("productReviews", JoinType.LEFT);
//                query.groupBy(root.get("uuidProduct"));
//                query.having(criteriaBuilder
//                        .greaterThanOrEqualTo(criteriaBuilder.avg(productReview.get("rating")), ratingFilter*1.0));
//            }
//            // Apply sorting
//            if (StringUtils.hasLength(sortBy)) {
//                String orderBy = SPEnum.valueOf(sortBy).getAlias();
//                if(orderBy.equals("sale")) {
//                    Join<Product, OrderItem>  orderItem = root.join("orderItems", JoinType.LEFT);
//                    query.groupBy(root.get("uuidProduct"));
//                    query.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(orderItem.get("quantity"))));
//                }
//                else {
//                    if(StringUtils.hasLength(sortDirection)){
//                        if ("desc".equalsIgnoreCase(sortDirection)) {
//                            query.orderBy(criteriaBuilder.desc(root.get(orderBy)));
//                        } else {
//                            query.orderBy(criteriaBuilder.asc(root.get(orderBy)));
//                        }
//                    }
//                    else {
//                        query.orderBy(criteriaBuilder.desc(root.get(orderBy)));
//                    }
//                }
//            }
//
//            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//        };
//    }
//}