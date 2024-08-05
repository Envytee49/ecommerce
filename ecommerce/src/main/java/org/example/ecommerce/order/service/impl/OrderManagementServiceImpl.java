package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.common.constants.OrderStatus;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.shop.dto.request.RevenueRequest;
import org.example.ecommerce.shop.dto.response.RevenueResponse;
import org.example.ecommerce.shop.dto.response.TopBuyer;
import org.example.ecommerce.shop.dto.response.TopOrderedProduct;
import org.example.ecommerce.shop.dto.response.TopSellingProduct;
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.repository.OrderRepository;
import org.example.ecommerce.order.service.OrderManagementService;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderManagementServiceImpl implements OrderManagementService {
    private final OrderRepository orderRepository;
    @Transactional
    @Override
    public void declineOrder(String uuidOrder) {
        Order order = orderRepository.findById(uuidOrder)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST));
        order.setStatus(OrderStatus.ORDER_DECLINED);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void approveOrder(String uuidOrder) {
        Order order = orderRepository.findById(uuidOrder)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST));
        order.setStatus(OrderStatus.ORDER_APPROVED);
        orderRepository.save(order);
    }


}
