package org.example.ecommerce.common.constants;

public enum DiscountType {
    PERCENTAGE {
        @Override
        public double getDiscount(double discount, double subTotal) {
            return discount;
        }
    }, FIXED {
        @Override
        public double getDiscount(double discount, double subTotal) {
            return subTotal * (1 - discount);
        }
    };

    public abstract double getDiscount(double discount, double subTotal);
}
