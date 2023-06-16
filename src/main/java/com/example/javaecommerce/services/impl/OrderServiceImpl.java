package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.exception.ResourceNotFoundException;
import com.example.javaecommerce.mapper.OrderMapper;
import com.example.javaecommerce.model.entity.*;
import com.example.javaecommerce.model.request.CartItemRequest;
import com.example.javaecommerce.model.request.OrderRequest;
import com.example.javaecommerce.model.response.OrderResponse;
import com.example.javaecommerce.repository.OrderDetailRepository;
import com.example.javaecommerce.repository.OrderRepository;
import com.example.javaecommerce.repository.ProductRepository;
import com.example.javaecommerce.repository.UserRepository;
import com.example.javaecommerce.services.OrderService;

import com.example.javaecommerce.utils.JWTSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final OrderMapper orderMapper;

    @Override
    public List<OrderResponse> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        return orderMapper.toListOrderResponse(orderEntities);
    }

    @Override
    public OrderResponse addOrder(final OrderRequest orderRequest) {
        OrderEntity order = new OrderEntity();
        //su dung jwt security de find out user
        var signedUser = JWTSecurity.getJWTUserInfo().orElseThrow();
        var user = userRepository.findById(signedUser.getId()).orElseThrow();
        order.setAddress(orderRequest.getAddress());
        order.setCity(orderRequest.getCity());
        order.setCountry(orderRequest.getCountry());
        order.setEmail(orderRequest.getEmail());
        order.setPhone(orderRequest.getPhone());
        order.setFirstName(orderRequest.getFirstName());
        order.setLastName(orderRequest.getLastName());
        order.setPostalCode(orderRequest.getPostalCode());
        order.setPhone(orderRequest.getPhone());

        List<OrderDetailEntity> orderDetails = order.getOrderDetails();

        for (CartItemRequest cartItem : orderRequest.getCartItems()) {
            Long productId = Long.valueOf(cartItem.getProductId());

            ProductEntity product = productRepository.findById(productId).get();

            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setTotal(cartItem.getPrice() * cartItem.getQuantity());
            orderDetailRepository.save(orderDetail);
            orderDetails.add(orderDetail);
        }
        order.setOrderDetails(orderDetails);
        order.setUser(user);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getOrderById(final Long orderID) {
        OrderEntity orderEntity = orderRepository.findById(orderID)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderID));
        return orderMapper.toOrderResponse(orderEntity);
    }

    @Override
    public void deleteOrder(final Long orderID) throws Exception {
        OrderDetailEntity orderDetailEntity = orderDetailRepository.findAll().stream().filter(
                        order -> Objects.equals(order.getId(), orderID))
                .findFirst()
                .orElse(null);
        if (null != orderDetailEntity) {
            throw new Exception("order detail is existed");
        } else {
            orderRepository.deleteById(orderID);
        }
    }

    @Override
    public OrderResponse updateOrder(final OrderRequest orderRequest, final Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .map(orderDetail -> {
                    orderDetail.setEmail(orderRequest.getEmail());
                    orderDetail.setAddress(orderRequest.getAddress());
                    orderDetail.setCity(orderRequest.getCity());
                    orderDetail.setCountry(orderRequest.getCountry());
                    orderDetail.setPhone(orderRequest.getPhone());
                    orderDetail.setLastName(orderRequest.getLastName());
                    orderDetail.setFirstName(orderRequest.getFirstName());
                    orderDetail.setPostalCode(orderRequest.getPostalCode());
                    return orderRepository.save(orderDetail);
                }).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return orderMapper.toOrderResponse(orderEntity);
    }
}
