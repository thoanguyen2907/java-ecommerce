package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.exception.ResourceNotFoundException;
import com.example.javaecommerce.mapper.OrderDetailMapper;
import com.example.javaecommerce.model.entity.OrderDetailEntity;
import com.example.javaecommerce.model.request.OrderDetailRequest;
import com.example.javaecommerce.model.response.OrderDetailResponse;
import com.example.javaecommerce.repository.OrderDetailRepository;
import com.example.javaecommerce.services.OrderDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    private final OrderDetailMapper orderDetailMapper;
    @Override
    public List<OrderDetailResponse> getAllOrderDetails() {
        List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findAll();
        return orderDetailMapper.toListOrderDetailResponse(orderDetailEntities);
    }

    @Override
    public OrderDetailResponse addOrderDetail(OrderDetailRequest orderDetailRequest) {
        OrderDetailEntity orderDetailEntity = Converter.toModel(orderDetailRequest, OrderDetailEntity.class);
        orderDetailRepository.save(orderDetailEntity);
        return orderDetailMapper.toOrderDetailResponse(orderDetailEntity);
    }

    @Override
    public OrderDetailResponse getOrderDetailById(Long orderDetailId) {
        OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderDetailId));
        return orderDetailMapper.toOrderDetailResponse(orderDetailEntity);
    }

    @Override
    public void deleteOrderDetail(Long orderDetailId) throws Exception {
        orderDetailRepository.deleteById(orderDetailId);
    }

    @Override
    public OrderDetailResponse updateOrderDetail(OrderDetailRequest orderDetailRequest, Long id) {
        OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(id)
                .map(orderDetail -> {
                    orderDetail.setTotal(orderDetailRequest.getTotal());
                    return orderDetailRepository.save(orderDetail);
                }).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return orderDetailMapper.toOrderDetailResponse(orderDetailEntity);
    }
}
