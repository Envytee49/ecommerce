package org.example.ecommerce.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.util.Utils;
@Table(name = "cancel_order_reason")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelledOrderReason {
    @Id
    @Builder.Default
    @Column(name = "uuid_reason")
    private String uuidReason = Utils.getUuid();
    @Column(name = "reason")
    private String reason;
}
