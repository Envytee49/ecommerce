package org.example.ecommerce.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class UserAddressResponse {
    private List<UserAddressDetail> userAddresses;
}
