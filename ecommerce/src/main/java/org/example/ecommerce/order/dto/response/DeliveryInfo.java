package org.example.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeliveryInfo {
    private String mobile;
    private String city;
    private String street;
    private String district;
    private Integer postalCode;
    private String receiver;
}
