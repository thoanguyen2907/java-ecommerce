package com.example.javaecommerce.services.impl;

import com.example.javaecommerce.exception.EcommerceRunTimeException;
import com.example.javaecommerce.exception.ErrorCode;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public List<OrderResponse> getAllOrders() {
        try {
            List<OrderEntity> orderEntities = orderRepository.findAll();
            logger.info("Get all orders with pagination successfully");
            return orderMapper.toListOrderResponse(orderEntities);
        } catch (Exception e) {
            logger.info("Failed to get all orders with pagination", e);
            throw new RuntimeException("Failed to get all orders with pagination");
        }
    }

    @Override
    public OrderResponse addOrder(final OrderRequest orderRequest) {
        try {
            OrderEntity order = new OrderEntity();
            //su dung jwt security de find out user
            var signedUser = JWTSecurity.getJWTUserInfo().orElseThrow();
            var user = userRepository.findById(signedUser.getId()).orElseThrow();
            logger.info("user is found by id {}", user.getId());
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
            logger.info("create order detail array");

            for (CartItemRequest cartItem : orderRequest.getCartItems()) {
                Long productId = Long.valueOf(cartItem.getProductId());

                ProductEntity product = productRepository.findById(productId).orElseThrow(
                        () -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
                logger.info("product is found wih id {} ", product.getId());
                OrderDetailEntity orderDetail = new OrderDetailEntity();
                logger.info("create order detail array");
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setTotal(cartItem.getPrice() * cartItem.getQuantity());
                orderDetailRepository.save(orderDetail);
                orderDetails.add(orderDetail);
                logger.info("add order detail item to order detail array");
            }
            order.setOrderDetails(orderDetails);
            order.setUser(user);
            orderRepository.save(order);
            logger.info("save order successfully");
            return orderMapper.toOrderResponse(order);
        } catch (Exception e) {
            logger.info("Failed to create order ", e);
            throw new RuntimeException("Failed to get create order");
        }
    }

    @Override
    public OrderResponse getOrderById(final Long orderId) {
        try {
            OrderEntity orderEntity = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Get order successfully, with id {}", orderId);
            return orderMapper.toOrderResponse(orderEntity);
        } catch (Exception e) {
            logger.info("Failed to get order by id", e);
            throw new RuntimeException("Failed to get order by id", e);
        }
    }

    @Override
    public void deleteOrder(final Long orderID) {
        try {
            OrderDetailEntity orderDetailEntity = orderDetailRepository.findAll().stream().filter(
                            order -> Objects.equals(order.getId(), orderID))
                    .findFirst()
                    .orElse(null);
            if (null != orderDetailEntity) {
                logger.info("this order contains order details, cannot delete !");
                throw new EcommerceRunTimeException(ErrorCode.ITEM_EXISTED);
            } else {
                orderRepository.deleteById(orderID);
                logger.info("delete order by id successfully: {} ", orderID);
            }
        } catch (Exception e) {
            logger.info("Failed to delete order by id ", e);
            throw new RuntimeException("Failed to delete order by id", e);
        }
    }

    @Override
    public OrderResponse updateOrder(final OrderRequest orderRequest, final Long id) {
        try {
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
                    }).orElseThrow(() -> new EcommerceRunTimeException(ErrorCode.ID_NOT_FOUND));
            logger.info("Update order successfully by id {} ", id);
            return orderMapper.toOrderResponse(orderEntity);
        } catch (Exception e) {
            logger.info("Failed to update order", e);
            throw new RuntimeException("Failed to order category");
        }
    }
}
