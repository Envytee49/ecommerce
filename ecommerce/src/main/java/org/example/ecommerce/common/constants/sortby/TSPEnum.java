package org.example.ecommerce.common.constants.sortby;

import lombok.Getter;

/**
 * Top Selling Product Enum - Sort By
 * */
@Getter
public enum TSPEnum implements AliasProvider{
    SALES("unitsSold"),
    ORDERS("orderCounts");
    private final String alias;

    TSPEnum(String alias) {
        this.alias = alias;
    }
}
