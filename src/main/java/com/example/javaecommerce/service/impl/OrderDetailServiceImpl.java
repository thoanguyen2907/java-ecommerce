package com.example.javaecommerce.service.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.model.entity.OrderDetailEntity;
import com.example.javaecommerce.model.request.OrderDetailRequest;
import com.example.javaecommerce.model.response.OrderDetailResponse;
import com.example.javaecommerce.repository.OrderDetailRepository;
import com.example.javaecommerce.service.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public List<OrderDetailResponse> getAllOrderDetails() {
        List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findAll();
        return Converter.toList(orderDetailEntities, OrderDetailResponse.class);
    }

    @Override
    public OrderDetailResponse addOrderDetail(OrderDetailRequest orderDetailRequest) {
        OrderDetailEntity orderDetailEntity = Converter.toModel(orderDetailRequest, OrderDetailEntity.class);
        orderDetailRepository.save(orderDetailEntity);
        return Converter.toModel(orderDetailEntity, OrderDetailResponse.class);
    }

    @Override
    public OrderDetailResponse getOrderDetailById(Long orderDetailID) {
        OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(orderDetailID).orElseThrow(() -> new IllegalStateException(
                "order with id" + " does not exist"
        ));
        return Converter.toModel(orderDetailEntity, OrderDetailResponse.class);
    }

    @Override
    public void deleteOrderDetail(Long orderDetailID) throws Exception {
        orderDetailRepository.deleteById(orderDetailID);
    }

    @Override
    public OrderDetailResponse updateOrderDetail(OrderDetailRequest orderDetailRequest, Long id) {
        OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(id)
                .map(orderDetail -> {
                    orderDetail.setEmail(orderDetailRequest.getEmail());
                    orderDetail.setPrice(orderDetailRequest.getPrice());
                    orderDetail.setQuantity(orderDetailRequest.getQuantity());
                    orderDetail.setTotal(orderDetailRequest.getTotal());
                    orderDetail.setImage(orderDetailRequest.getImage());
                    return orderDetailRepository.save(orderDetail);
                }).orElseThrow(() -> new IllegalStateException(
                        "order detail  with id " + id + " does not exist"
                ));
        return Converter.toModel(orderDetailEntity, OrderDetailResponse.class);
    }
}
