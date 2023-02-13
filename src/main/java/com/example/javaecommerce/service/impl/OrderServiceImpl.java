package com.example.javaecommerce.service.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.model.entity.OrderDetailEntity;
import com.example.javaecommerce.model.entity.OrderEntity;
import com.example.javaecommerce.model.request.OrderRequest;
import com.example.javaecommerce.model.response.OrderResponse;
import com.example.javaecommerce.repository.OrderDetailRepository;
import com.example.javaecommerce.repository.OrderRepository;
import com.example.javaecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderResponse> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        return Converter.toList(orderEntities, OrderResponse.class);
    }

    @Override
    public OrderResponse addOrder(OrderRequest orderRequest) {
        OrderEntity orderEntity = Converter.toModel(orderRequest, OrderEntity.class);
        orderRepository.save(orderEntity);
        return Converter.toModel(orderEntity, OrderResponse.class);
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
        OrderDetailEntity orderDetailEntity =   orderDetailRepository.findAll().stream().filter(
                        order -> Objects.equals(order.getOrder().getId(), orderID)   )
                .findFirst()
                .orElse(null);
        if (null != orderDetailEntity) {
            throw new Exception("order detail is existed");
        }  else {
            orderRepository.deleteById(orderID);
        }
    }

    @Override
    public OrderResponse updateOrder(OrderRequest orderRequest, Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .map(orderDetail -> {
                   orderDetail.setEmail(orderRequest.getEmail());
                   orderDetail.setAddress(orderRequest.getAddress());
                   orderDetail.setQuantity(orderRequest.getQuantity());
                   orderDetail.setPrice(orderRequest.getPrice());
                   orderDetail.setCity(orderRequest.getCity());
                   orderDetail.setCountry(orderRequest.getCountry());
                   orderDetail.setPhone(orderRequest.getPhone());
                   orderDetail.setLast_name(orderRequest.getLast_name());
                   orderDetail.setFirst_name(orderRequest.getFirst_name());
                   orderDetail.setPostal_code(orderRequest.getPostal_code());
                    return orderRepository.save(orderDetail);
                }).orElseThrow(() -> new IllegalStateException(
                        "order detail  with id " + id + " does not exist"
                ));
        return Converter.toModel(orderEntity, OrderResponse.class);
    }
}
