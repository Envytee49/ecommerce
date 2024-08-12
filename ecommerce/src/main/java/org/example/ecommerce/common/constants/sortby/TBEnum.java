package org.example.ecommerce.common.constants.sortby;

import lombok.Getter;

/**
  * Top Buyer Enum  - Sort By
  * */
@Getter
public enum TBEnum implements AliasProvider{
    ORDERS("orderCounts"),
    SPENDING("totalSpend");
     private final String alias;

     TBEnum(String alias) {
         this.alias = alias;
     }
}
