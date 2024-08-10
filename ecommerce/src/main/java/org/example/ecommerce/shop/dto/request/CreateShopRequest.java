package org.example.ecommerce.shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateShopRequest {
    @NotBlank(message = "name must not be blank")
    private String name;
    @NotBlank
    protected String mobile;
    @NotBlank
    protected String city;
    @NotBlank
    protected String street;
    @NotBlank
    protected String district;
    @NotBlank
    protected Integer postalCode;
    @NotBlank
    private String sellerName;
}
