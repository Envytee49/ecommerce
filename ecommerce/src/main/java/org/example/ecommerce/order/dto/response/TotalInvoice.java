package org.example.ecommerce.order.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalInvoice {
    private double subTotal;
    private double totalAmount;
    private double discount;
    private double shipping;

    public void updateInvoice(double shipping, double subTotal, double discount, double totalAmount) {
        this.shipping += shipping;
        this.subTotal += subTotal;
        this.discount += discount;
        this.totalAmount += totalAmount;
    }
}
