package org.example.ecommerce.common.constants;

public enum DiscountType {
    FIXED {
        @Override
        public double getDiscount(double discount, double subTotal, double discountCap) {
            return discount;
        }
    }, PERCENTAGE {
        @Override
        public double getDiscount(double discount, double subTotal, double discountCap) {
            return Math.min(subTotal * discount, discountCap);
        }
    };

    public abstract double getDiscount(double discount, double subTotal, double discountCap);
}
