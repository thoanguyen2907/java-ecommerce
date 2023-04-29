package com.example.javaecommerce.mapper;

import com.example.javaecommerce.model.entity.OrderEntity;
import com.example.javaecommerce.model.response.OrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(OrderEntity orderEntity);
}
