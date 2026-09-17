package com.macias34.codemastery.order.controller;

import com.macias34.codemastery.order.dto.CreateOrderRequestDto;
import com.macias34.codemastery.order.dto.OrderDto;
import com.macias34.codemastery.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "order")
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<Void> createOrder(
            @RequestBody CreateOrderRequestDto dto
    ){
        orderService.createOrder(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        List<OrderDto> orderDto = orderService.getOrders();
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(
            @PathVariable("id") int id
    ){
        OrderDto orderDto = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDto);
    }
}