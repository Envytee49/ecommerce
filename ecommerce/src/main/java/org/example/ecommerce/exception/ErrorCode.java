package org.example.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN(1, "Unknown Error"),

    BAD_REQUEST(400, "Bad Request" ),

    UNAUTHENTICATED( 401 ,"Unauthenticated" ),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error" ),

    INVALID_PARAMETER_DATA(998, null),

    PARAMETER_NOT_VALID(999, null),

    INVALID_PARAMETER_TYPE(1000, "Parameter type is not suitable."),

    INVALID_DATA_FORMAT(1001, "Invalid data format."),

    NO_RESOURCE_FOUND(1002, "No resource found."),

    PRODUCT_NOT_FOUND(1003, "Product Not Found"),

    BRAND_NOT_FOUND(1004, "Brand Not Found"),

    CART_ITEM_NOT_FOUND(1005, "Cart Item Not Found"),

    CART_NOT_EXIST(1006, "Cart Not Exist Or Is Empty"),

    USERNAME_NOT_FOUND(1007, "Username Not Found"),

    USER_NOT_FOUND(1008, "User Not Found"),
    BAD_CREDENTIALS(1009, "Username Or Password Incorrect"),

    ROLE_NOT_FOUND(1010, "Role Not Found"),

    EMAIL_EXISTED(1011, "Email Existed"),

    VOUCHER_NOT_FOUND(1012, "Voucher Not Found"),

    NOT_ENOUGH_VOUCHER(1013, "Not Enough Voucher"),

    UNDER_MINIMUM_SPEND(1013, "Minimum Spend Is Below Threshold"),

    ADDRESS_NOT_FOUND(1014, "Address Not Found"),

    SHOP_NOT_FOUND(1015,"Shop Not Found" ),

    MAX_VOUCHER_USAGE_REACHED(1016, "Max Voucher Usage Reached" ),

    VOUCHER_ALREADY_REDEEMED(1017,"Voucher Already Redeemed" ),

    VOUCHER_CODE_EXISTED(1018, "Voucher Code Existed" ),

    WRONG_OLD_PASSWORD(1019, "Wrong Old Password" ),

    CANCEL_ORDER_REASON_NOT_EXIST(1020,"Cancel Order Reason Not Exist" ),

    ORDER_NOT_FOUND(1021, "Order Not Found" ),

    ORDER_CANNOT_BE_CANCELED(1022,"ORDER CANNOT BE CANCELED" ),

    INVALID_DATE_VALUE(1023, null ),

    NO_ADDRESS_AVAILABLE(1024, "No Address Available" ),

    INVALID_DISCOUNT_VALUE(1025, "Percentage Value Must Be Between 0 - 100%"),

    SHOP_NAME_EXISTED(1026, "Shop Name Existed" ),

    CATEGORY_NOT_FOUND(1027, "Category Not Found" ),

    MISSING_VARIANT_OPTION(1028, "Please select all least one product variant" ),

    PRODUCT_VARIANT_NOT_FOUND(1029, "Product Variant Not Found" ),

    NOT_ENOUGH_STOCK(1030, "Not Enough Stock" ),

    INVALID_PRICE_RANGE(1031, "Invalid Price Range" ),

    MISMATCH_QUANTITY(1032, "Total quantity and sku quantity does not match" );

    private int code;

    private String message;

    public ErrorCode setMessage(String message) {
        this.message = message;
        return this;
    }

}
