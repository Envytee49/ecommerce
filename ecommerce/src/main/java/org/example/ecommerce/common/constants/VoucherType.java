package org.example.ecommerce.common.constants;

public enum VoucherType {
    ALL_SHOP(Role.SELLER),
    PRODUCTS(Role.SELLER),
    FREE_SHIPPING(Role.ADMIN);

    private final Role role;

    VoucherType(Role role) {
        this.role = role;
    }
}
