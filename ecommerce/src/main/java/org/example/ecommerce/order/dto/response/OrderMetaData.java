package org.example.ecommerce.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.PaymentMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderMetaData {
    private PaymentMethod paymentMethod;
    private LocalDateTime orderTime;
    private LocalDateTime paymentTime;
    private LocalDateTime shipTime;
    private LocalDateTime completedTime;
}
