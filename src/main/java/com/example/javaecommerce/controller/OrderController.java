package com.example.javaecommerce.controller;

import com.example.javaecommerce.model.request.OrderRequest;
import com.example.javaecommerce.model.response.OrderResponse;
import com.example.javaecommerce.services.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public OrderResponse addOrder(@RequestBody final OrderRequest orderRequest) {
        return orderService.addOrder(orderRequest);
    }

    @DeleteMapping(path = "{orderId}")
    public void deleteOrder(@PathVariable("orderId") final Long orderId) throws Exception {
        orderService.deleteOrder(orderId);
    }

    @PutMapping
    public OrderResponse updateOrder(@RequestBody final OrderRequest orderRequest, final Long id) {
        return orderService.updateOrder(orderRequest, id);
    }
}
