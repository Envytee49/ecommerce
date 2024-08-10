package org.example.ecommerce.common.util;

import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Utils {
    public static String getUuid() {
        String uuidString = UUID.randomUUID().toString();
        if (uuidString.length() > 40) {
            return uuidString.substring(0, 40);
        } else {
            return uuidString;
        }
    }

    /**
     * get T 'thirty' S 'six' Uuid
     */
    public static String getTSUuid() {
        String uuidString = UUID.randomUUID().toString();
        if (uuidString.length() > 36) {
            return uuidString.substring(0, 36);
        } else {
            return uuidString;
        }
    }

    public static <E extends Enum<E>> Sort getSortStrategy(List<String> sortBy, Class<E> sortByEnum, String sortDirection) {
        checkEnumNotContainsValues(sortBy, sortByEnum);
        checkEnumNotContainsValue(sortDirection, Sort.Direction.class, "sortDirection");
        return Sort.by(Sort.Direction.fromString(sortDirection), sortBy.toArray(new String[0]));
    }

    private static <E extends Enum<E>> void checkEnumNotContainsValues(List<String> value, Class<E> enumType) {
        if (value.isEmpty())
            throw new AppException(ErrorCode.INVALID_DATE_VALUE.setMessage("sortBy :At least one property must be given"));
        value.forEach(v -> checkEnumNotContainsValue(v, enumType, "sortBy"));
    }

    private static <E extends Enum<E>> void checkEnumNotContainsValue(String value, Class<E> enumType, String requestParam) {
        try {
            Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_DATE_VALUE
                    .setMessage(
                            requestParam +
                                    " should be " +
                                    Arrays.toString(enumType.getEnumConstants()))
            );
        }
    }

    public static double computeCartItemSubTotal(List<CartItem> cartItems) {
        if (cartItems == null) {
            return 0;
        }

        return cartItems.stream()
                .mapToDouble(cartItem -> {
                    double discountMultiplier = cartItem.getDiscount() == 0 ? 1 : cartItem.getDiscount();
                    return cartItem.getQuantity() * cartItem.getUnitPrice() * discountMultiplier;
                })
                .sum();
    }
}
