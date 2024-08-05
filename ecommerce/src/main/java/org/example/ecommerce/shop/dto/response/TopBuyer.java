package org.example.ecommerce.shop.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TopBuyer {
    private String uuidUser;
    private String username;
    private int orderCounts;
    private double totalSpend;
}
