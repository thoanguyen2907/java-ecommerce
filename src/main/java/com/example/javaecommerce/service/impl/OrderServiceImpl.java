package com.example.javaecommerce.service.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.model.entity.*;
import com.example.javaecommerce.model.request.CartItemRequest;
import com.example.javaecommerce.model.request.OrderRequest;
import com.example.javaecommerce.model.response.OrderResponse;
import com.example.javaecommerce.repository.OrderDetailRepository;
import com.example.javaecommerce.repository.OrderRepository;
import com.example.javaecommerce.repository.ProductRepository;
import com.example.javaecommerce.repository.UserRepository;
import com.example.javaecommerce.service.OrderService;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderResponse> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        return Converter.toList(orderEntities, OrderResponse.class);
    }

    @Override
    public OrderResponse addOrder(OrderRequest orderRequest) {
        OrderEntity order = new OrderEntity();
        Long userId = Long.valueOf(orderRequest.getUserId());
        //user tạm thời, sẽ sử dụng jwt lấy user principal bằng securityContext
        UserEntity user = userRepository.findById(userId).get();
        order.setUser(user);
        order.setAddress(orderRequest.getAddress());
        order.setCity(orderRequest.getCity());
        order.setCountry(orderRequest.getCountry());
        order.setEmail(orderRequest.getEmail());
        order.setPhone(orderRequest.getPhone());
        order.setFirstName(orderRequest.getFirstName());
        order.setLastName(orderRequest.getLastName());
        order.setPostalCode(orderRequest.getPostalCode());

        List<OrderDetailEntity> orderDetails = order.getOrderDetails();

        for (CartItemRequest cartItem : orderRequest.getCartItems()) {
            Long productId = Long.valueOf(cartItem.getProductId());

            ProductEntity product = productRepository.findById(productId).get();

            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setTotal(cartItem.getPrice() * cartItem.getQuantity());
            orderDetails.add(orderDetail);
        }
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);
        //fix: se ap dung mapstruct cho truong hop loc du lieu
        OrderResponse response = new OrderResponse(
                order.getAddress(),
                order.getCity(),
                order.getCountry(),
                order.getEmail(),
                order.getPostalCode(),
                order.getFirstName(),
                order.getLastName(),
                order.getPhone(),
                order.getOrderDetails()
        );
        return response;
    }

    @Override
    public OrderResponse getOrderById(Long orderID) {
        OrderEntity orderEntity = orderRepository.findById(orderID).orElseThrow(() -> new IllegalStateException(
                "category with id" + " does not exist"
        ));
        return Converter.toModel(orderEntity, OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long orderID) throws Exception {
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
    public OrderResponse updateOrder(OrderRequest orderRequest, Long id) {
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
                }).orElseThrow(() -> new IllegalStateException(
                        "order detail  with id " + id + " does not exist"
                ));
        return Converter.toModel(orderEntity, OrderResponse.class);
    }
}
