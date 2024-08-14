package org.example.ecommerce.product.aop;

import org.example.ecommerce.common.constants.sortby.SPEnum;
import org.example.ecommerce.common.util.Utils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SearchProductAOP {
    public void checkSearchProductParams(Double minPrice,
                                         Double maxPrice,
                                         String sortBy,
                                         String sortDirection) {

        if (minPrice != null && maxPrice != null) {
            if(minPrice > maxPrice)
                throw new AppException(ErrorCode.INVALID_PRICE_RANGE);
        }
        if(StringUtils.hasLength(sortBy))
            Utils.checkEnumNotContainsValue(sortBy, SPEnum.class, "sortBy");
        if(StringUtils.hasLength(sortDirection))
            Utils.checkEnumNotContainsValue(sortDirection, Sort.Direction.class, "sortDirection");
    }
}
