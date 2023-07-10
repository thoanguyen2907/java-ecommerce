package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.OrderDetailRequest;
import com.example.javaecommerce.model.response.OrderDetailResponse;
import com.example.javaecommerce.services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/order-detail")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping
    public ResponseEntity<?> getAllOrderDetails() {
        List<OrderDetailResponse> orderDetailResponses = orderDetailService.getAllOrderDetails();
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PostMapping
    public OrderDetailResponse addOrderDetail(@RequestBody final OrderDetailRequest orderDetailRequest) {
        return orderDetailService.addOrderDetail(orderDetailRequest);
    }

    @DeleteMapping(path = "{orderDetailId}")
    public void deleteOrderDetail(@PathVariable("orderDetailId") final Long orderDetailId) throws Exception {
        orderDetailService.deleteOrderDetail(orderDetailId);
    }

    @PutMapping
    public OrderDetailResponse updateOrderDetail(@RequestBody final OrderDetailRequest orderDetailRequest, final Long id) {
        return orderDetailService.updateOrderDetail(orderDetailRequest, id);
    }
}
