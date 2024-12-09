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
    public ResponseEntity<OrderDetailResponse> addOrderDetail(@RequestBody final OrderDetailRequest orderDetailRequest) {
        var orderDetailResponse = orderDetailService.addOrderDetail(orderDetailRequest);
        return ResponseEntity.ok(orderDetailResponse);
    }

    @DeleteMapping(path = "{orderDetailId}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable("orderDetailId") final Long orderDetailId) throws Exception {
        orderDetailService.deleteOrderDetail(orderDetailId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<OrderDetailResponse> updateOrderDetail(@RequestBody final OrderDetailRequest orderDetailRequest, final Long id) {
        var orderDetailResponse = orderDetailService.updateOrderDetail(orderDetailRequest, id);
        return ResponseEntity.ok(orderDetailResponse);
    }
}
