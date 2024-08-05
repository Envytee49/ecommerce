package org.example.ecommerce.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.PaymentMethod;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CancelledOrderDetailResponse {
//    private OrderItemResponse orderItem;
    private String requestedBy;
    private String requestedAt;
    private String reason;
    private PaymentMethod paymentMethod;
}
