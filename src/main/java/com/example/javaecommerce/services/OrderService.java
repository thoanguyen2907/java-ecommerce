package com.example.javaecommerce.services;

import com.example.javaecommerce.model.request.OrderRequest;
import com.example.javaecommerce.model.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllOrders();

    OrderResponse addOrder(OrderRequest orderRequest);

    OrderResponse getOrderById(Long orderID);

    void deleteOrder(Long orderDetailId) throws Exception;

    OrderResponse updateOrder(OrderRequest orderRequest, Long id);
}
