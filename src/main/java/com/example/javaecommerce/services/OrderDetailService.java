package com.example.javaecommerce.services;

import com.example.javaecommerce.model.request.OrderDetailRequest;
import com.example.javaecommerce.model.response.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponse> getAllOrderDetails();

    OrderDetailResponse addOrderDetail(OrderDetailRequest orderDetailRequest);

    OrderDetailResponse getOrderDetailById(Long orderDetailId);

    void deleteOrderDetail(Long orderDetailId) throws Exception;

    OrderDetailResponse updateOrderDetail(OrderDetailRequest orderDetailRequest, Long id);
}
