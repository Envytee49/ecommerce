package org.example.ecommerce.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ecommerce.common.constants.Status;

import java.time.LocalDateTime;
@Table(name = "cancelled_order")
@Entity
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelledOrder {
    @Id
    @Column(name = "uuid_order")
    private String uuidOrder;
    @Column(name = "uuid_reason")
    private String uuidReason;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
    @PrePersist
    public void prePersist() {
        requestedAt = LocalDateTime.now();
    }
}
