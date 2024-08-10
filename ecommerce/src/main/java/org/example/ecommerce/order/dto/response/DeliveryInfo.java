package org.example.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.user.model.UserAddress;

@Builder
@Getter
public class DeliveryInfo {
    private String uuidUAddress;
    private String mobile;
    private String city;
    private String street;
    private String district;
    private String receiver;

    public static DeliveryInfo from(UserAddress userAddress) {
        return DeliveryInfo.builder()
                .uuidUAddress(userAddress.getUuidUAddress())
                .district(userAddress.getDistrict())
                .city(userAddress.getCity())
                .street(userAddress.getStreet())
                .mobile(userAddress.getMobile())
                .receiver(userAddress.getReceiverName())
                .build();
    }
}
