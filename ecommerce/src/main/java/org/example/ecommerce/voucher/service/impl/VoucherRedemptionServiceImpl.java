package org.example.ecommerce.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.voucher.model.Voucher;
import org.example.ecommerce.voucher.model.VoucherConstraint;
import org.example.ecommerce.voucher.model.VoucherRedemption;
import org.example.ecommerce.voucher.model.VoucherRedemptionId;
import org.example.ecommerce.voucher.repository.VoucherConstraintRepository;
import org.example.ecommerce.voucher.repository.VoucherRedemptionRepository;
import org.example.ecommerce.voucher.repository.VoucherRepository;
import org.example.ecommerce.voucher.service.VoucherRedemptionService;
import org.example.ecommerce.voucher.service.VoucherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoucherRedemptionServiceImpl implements VoucherRedemptionService {
    private final VoucherRepository voucherRepository;
    private final VoucherConstraintRepository voucherConstraintRepository;
    private final VoucherRedemptionRepository voucherRedemptionRepository;
    @Override
    @Transactional
    public void saveVoucherRedemption(String uuidVoucher, String uuidUser) {
        Voucher voucher = voucherRepository.findById(uuidVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        VoucherConstraint voucherConstraint = voucherConstraintRepository.findById(uuidVoucher).get();
        if(!voucher.isVisible())
            throw new AppException(ErrorCode.BAD_REQUEST);

        VoucherRedemptionId voucherRedemptionId = VoucherRedemptionId.builder()
                .uuidVoucher(uuidVoucher)
                .uuidUser(SecurityUtils.getCurrentUserUuid())
                .build();
        if (voucherRedemptionRepository.existsById(voucherRedemptionId))
            throw new AppException(ErrorCode.VOUCHER_ALREADY_REDEEMED);

        if (voucher.getQuantity() - 1 < 0)
            throw new AppException(ErrorCode.NOT_ENOUGH_VOUCHER);

        voucher.setQuantity(voucher.getQuantity() - 1);
        voucherRedemptionRepository.save(
                VoucherRedemption
                        .builder()
                        .uuidUser(uuidUser)
                        .uuidVoucher(voucher.getUuidVoucher())
                        .usage(voucherConstraint.getMaxUsage())
                        .build());
        voucherRepository.save(voucher);
    }
}
