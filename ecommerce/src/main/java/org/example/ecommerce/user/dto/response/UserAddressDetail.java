package org.example.ecommerce.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.user.model.UserAddress;

@Builder
@Getter
public class UserAddressDetail {
    private String uuidUAddress;
    private String mobile;
    private String city;
    private String street;
    private String district;
    private Integer postalCode;
    private String receiver;

    public static UserAddressDetail from(UserAddress userAddress) {
        return UserAddressDetail.builder()
                .uuidUAddress(userAddress.getUuidUAddress())
                .mobile(userAddress.getMobile())
                .city(userAddress.getCity())
                .street(userAddress.getStreet())
                .district(userAddress.getDistrict())
                .postalCode(userAddress.getPostalCode())
                .receiver(userAddress.getReceiverName())
                .build();
    }
}
