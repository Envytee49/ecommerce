package org.example.ecommerce.shop.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
@Builder
@Getter
public class RevenueResponse {
    private LocalDate orderDate;
    private String uuidOrder;
    private double totalAmount;
}
