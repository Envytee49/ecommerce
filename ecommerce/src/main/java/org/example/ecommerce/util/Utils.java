package org.example.ecommerce.util;

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
}
