package org.example.ecommerce.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.voucher.model.*;
import org.example.ecommerce.voucher.repository.VoucherConstraintRepository;
import org.example.ecommerce.voucher.repository.VoucherInfoRepository;
import org.example.ecommerce.voucher.repository.VoucherRedemptionRepository;
import org.example.ecommerce.voucher.repository.ShopVoucherRepository;
import org.example.ecommerce.voucher.service.VoucherRedemptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoucherRedemptionServiceImpl implements VoucherRedemptionService {
    private final VoucherConstraintRepository voucherConstraintRepository;
    private final VoucherRedemptionRepository voucherRedemptionRepository;
    private final VoucherInfoRepository voucherInfoRepository;
    private final ShopVoucherRepository shopVoucherRepository;

    @Override
    @Transactional
    public void saveShopVoucherRedemption(String uuidVoucher, String uuidUser) {
        ShopVoucher shopVoucher = shopVoucherRepository.findById(uuidVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        VoucherInfo voucherInfo = voucherInfoRepository.findById(shopVoucher.getUuidVoucherInfo())
                .orElseThrow(() -> new AppException(ErrorCode.INTERNAL_SERVER_ERROR));
        VoucherConstraint voucherConstraint = voucherConstraintRepository.findById(shopVoucher.getUuidVoucherConstraint())
                .orElseThrow(() -> new AppException(ErrorCode.INTERNAL_SERVER_ERROR));
        if(!voucherInfo.isVisible())
            throw new AppException(ErrorCode.BAD_REQUEST);


        VoucherRedemptionId voucherRedemptionId = VoucherRedemptionId.builder()
                .uuidVoucher(uuidVoucher)
                .uuidUser(SecurityUtils.getCurrentUserUuid())
                .build();

        if (voucherRedemptionRepository.existsById(voucherRedemptionId)) {
            throw new AppException(ErrorCode.VOUCHER_ALREADY_REDEEMED);
        }

        if (voucherInfo.getQuantity() - 1 < 0)
            throw new AppException(ErrorCode.NOT_ENOUGH_VOUCHER);

        voucherInfo.setQuantity(voucherInfo.getQuantity() - 1);
        voucherRedemptionRepository.save(
                VoucherRedemption
                        .builder()
                        .uuidUser(uuidUser)
                        .uuidVoucher(uuidVoucher)
                        .usage(voucherConstraint.getMaxUsage())
                        .build());
        voucherInfoRepository.save(voucherInfo);
    }
}
