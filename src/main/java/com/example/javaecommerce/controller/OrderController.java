package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.OrderRequest;
import com.example.javaecommerce.model.response.OrderResponse;
import com.example.javaecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<OrderResponse> orderResponse = orderService.getAllOrders();
        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(@RequestBody final OrderRequest orderRequest) {
        var orderResponse = orderService.addOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }

    @DeleteMapping(path = "{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") final Long orderId) throws Exception {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody final OrderRequest orderRequest, final Long id) {
        var orderResponse = orderService.updateOrder(orderRequest, id);
        return ResponseEntity.ok(orderResponse);
    }
}
