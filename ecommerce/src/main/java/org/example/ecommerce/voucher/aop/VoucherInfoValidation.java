package org.example.ecommerce.voucher.aop;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.constants.DiscountType;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.repository.CategoryRepository;
import org.example.ecommerce.voucher.repository.VoucherInfoRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoucherInfoValidation {
    private final VoucherInfoRepository voucherInfoRepository;
    private final CategoryRepository categoryRepository;
    public void preCheck(String voucherCode, String uuidCategory, DiscountType discountType, double discount) {
        checkVoucherCodeExist(voucherCode);
        checkDiscountValueValid(discountType, discount);
        checkCategoryExist(uuidCategory);
    }
    public void checkVoucherCodeExist(String voucherCode) {
        if (voucherInfoRepository.findByVoucherCode(voucherCode).isPresent())
            throw new AppException(ErrorCode.VOUCHER_CODE_EXISTED);
    }

    public void checkCategoryExist(String uuidCategory) {
        if(uuidCategory == null) return;
        if(uuidCategory.isEmpty()) throw new AppException(ErrorCode.BAD_REQUEST.setMessage("uuidCategory is empty"));
        categoryRepository
                .findById(uuidCategory)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public void checkDiscountValueValid(DiscountType discountType, double discount) {
        if(discountType == DiscountType.PERCENTAGE && discount > 100)
            throw new AppException(ErrorCode.INVALID_DISCOUNT_VALUE);
    }
}
