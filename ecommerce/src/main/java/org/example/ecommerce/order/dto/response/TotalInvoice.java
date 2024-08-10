package org.example.ecommerce.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalInvoice {
    private double merchandiseSubTotal;
    private double shippingSubtotal;
    private double shippingDiscountSubtotal;
    private double voucherDiscount;
    private double totalPayment;

    public void updateInvoice(double merchandiseSubTotal,
                              double shippingSubtotal,
                              double shippingDiscountSubtotal,
                              double voucherDiscount,
                              double totalPayment) {
        this.merchandiseSubTotal += merchandiseSubTotal;
        this.shippingSubtotal += shippingSubtotal;
        this.shippingDiscountSubtotal += shippingDiscountSubtotal;
        this.voucherDiscount += voucherDiscount;
        this.totalPayment += totalPayment;
    }
}
