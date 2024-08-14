package org.example.ecommerce.common.constants.sortby;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;

/**
 * SearchProductEnum - Sort by
 * */
@Getter
public enum SPEnum implements AliasProvider{
    RELEVANCY("title"),
    // created time
    CTIME("createdDate"),
    PRICE("price"),
    SALES("sale");
    private final String alias;

    SPEnum(String alias) {
        this.alias = alias;
    }
}
