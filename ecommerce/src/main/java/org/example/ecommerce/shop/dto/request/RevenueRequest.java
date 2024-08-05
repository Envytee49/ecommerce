package org.example.ecommerce.shop.dto.request;

import lombok.Getter;
import org.example.ecommerce.common.dto.PageDtoIn;

import java.time.LocalDate;
@Getter
public class RevenueRequest {
    private PageDtoIn pageDtoIn;
    private LocalDate fromDate;
    private LocalDate toDate;
}
