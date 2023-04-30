package com.example.javaecommerce.mapper;

import com.example.javaecommerce.model.entity.OrderEntity;
import com.example.javaecommerce.model.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "orderEntity.user", target = "user")
    OrderResponse toOrderResponse(OrderEntity orderEntity);

    List<OrderResponse> toListOrderResponse(List<OrderEntity> orderEntityList);
}
