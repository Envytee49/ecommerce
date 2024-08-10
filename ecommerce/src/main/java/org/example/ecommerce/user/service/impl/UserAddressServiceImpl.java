package org.example.ecommerce.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.dto.response.DeliveryInfo;
import org.example.ecommerce.user.model.DefaultUserAddress;
import org.example.ecommerce.user.model.UserAddress;
import org.example.ecommerce.user.repository.DefaultUserAddressRepository;
import org.example.ecommerce.user.repository.UserAddressRepository;
import org.example.ecommerce.user.service.UserAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
    private static final Logger log = LoggerFactory.getLogger(UserAddressServiceImpl.class);
    private final UserAddressRepository userAddressRepository;
    private final DefaultUserAddressRepository defaultUserAddressRepository;
    @Override
    public UserAddress getUserAddress(String uuidUAddress) {
        log.info("getDeliveryInfo: {}", uuidUAddress);
        UserAddress userAddress;
        if (!StringUtils.hasLength(uuidUAddress)) {
            DefaultUserAddress defaultUserAddress = defaultUserAddressRepository
                    .findById(SecurityUtils.getCurrentUserUuid())
                    .orElseThrow(() -> new AppException(ErrorCode.NO_ADDRESS_AVAILABLE));
            userAddress = userAddressRepository
                    .findById(defaultUserAddress.getUuidUAddress()).get();
            return userAddress;
        }
        userAddress = userAddressRepository.findByUuidUAddressAndUuidUser(uuidUAddress, SecurityUtils.getCurrentUserUuid()).
                orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        return userAddress;
    }
}
