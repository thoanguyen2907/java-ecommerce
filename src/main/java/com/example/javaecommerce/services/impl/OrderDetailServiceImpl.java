package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.exception.EcommerceRunTimeException;
import com.example.javaecommerce.exception.ErrorCode;
import com.example.javaecommerce.mapper.OrderDetailMapper;
import com.example.javaecommerce.model.entity.OrderDetailEntity;
import com.example.javaecommerce.model.request.OrderDetailRequest;
import com.example.javaecommerce.model.response.OrderDetailResponse;
import com.example.javaecommerce.repository.OrderDetailRepository;
import com.example.javaecommerce.services.OrderDetailService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    private final OrderDetailMapper orderDetailMapper;

    private static final Logger logger = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

    @Override
    public List<OrderDetailResponse> getAllOrderDetails() {
        try {
            List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findAll();
            logger.info("Get all order details with pagination successfully");
            return orderDetailMapper.toListOrderDetailResponse(orderDetailEntities);
        } catch (Exception e) {
            logger.info("Failed to get all order details with pagination", e);
            throw new RuntimeException("Failed to get all order details with pagination");
        }
    }

    @Override
    public OrderDetailResponse addOrderDetail(final OrderDetailRequest orderDetailRequest) {
        try {
            OrderDetailEntity orderDetailEntity = Converter.toModel(orderDetailRequest, OrderDetailEntity.class);
            orderDetailRepository.save(orderDetailEntity);
            logger.info("Create order successfully!");
            return orderDetailMapper.toOrderDetailResponse(orderDetailEntity);
        } catch (Exception e) {
            logger.info("Failed to create order");
            throw new RuntimeException("Failed to create order", e);
        }
    }

    @Override
    public OrderDetailResponse getOrderDetailById(final Long orderDetailId) {
        try {
            OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Get order detail successfully by id {}", orderDetailId);
            return orderDetailMapper.toOrderDetailResponse(orderDetailEntity);
        } catch (Exception e) {
            logger.info("Failed to get order detail by id", e);
            throw new RuntimeException("Failed to get order detail by id", e);
        }
    }

    @Override
    public void deleteOrderDetail(final Long orderDetailId) {
        try {
            var orderDetails = orderDetailRepository
                    .findById(orderDetailId)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            if (orderDetails != null) {
                logger.info("order detail is found by id {}", orderDetailId);
                orderDetailRepository.deleteById(orderDetailId);
                logger.info("delete order detail successfully by id {}", orderDetailId);
            }
        } catch (Exception e) {
            logger.info("Failed to get order detail by id", e);
            throw new RuntimeException("Failed to get order detail by id", e);
        }
    }

    @Override
    public OrderDetailResponse updateOrderDetail(final OrderDetailRequest orderDetailRequest, final Long id) {
        try {
            OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(id)
                    .map(orderDetail -> {
                        orderDetail.setTotal(orderDetailRequest.getTotal());
                        return orderDetailRepository.save(orderDetail);
                    }).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Update order details successfully by id {} ", id);
            return orderDetailMapper.toOrderDetailResponse(orderDetailEntity);
        } catch (Exception e) {
            logger.info("Failed to update order details", e);
            throw new RuntimeException("Failed to update order details");
        }
    }
}
