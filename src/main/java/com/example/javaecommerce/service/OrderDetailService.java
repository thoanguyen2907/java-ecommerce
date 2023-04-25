package com.example.javaecommerce.service;

import com.example.javaecommerce.model.request.OrderDetailRequest;
import com.example.javaecommerce.model.response.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponse> getAllOrderDetails();

    OrderDetailResponse addOrderDetail(OrderDetailRequest orderDetailRequest);

    OrderDetailResponse getOrderDetailById(Long orderDetailID);

    void deleteOrderDetail(Long orderDetailID) throws Exception;

    OrderDetailResponse updateOrderDetail(OrderDetailRequest orderDetailRequest, Long id);
}
