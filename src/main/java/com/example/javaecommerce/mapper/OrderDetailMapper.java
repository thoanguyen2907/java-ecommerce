package com.example.javaecommerce.mapper;

import com.example.javaecommerce.model.entity.OrderDetailEntity;
import com.example.javaecommerce.model.response.OrderDetailResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailResponse toOrderDetailResponse (OrderDetailEntity orderDetailEntity);
    List<OrderDetailResponse> toListOrderDetailResponse(List<OrderDetailEntity> orderDetailEntityList );
}
