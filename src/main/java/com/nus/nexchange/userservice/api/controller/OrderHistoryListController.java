package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryListDTO;
import com.nus.nexchange.userservice.application.command.OrderHistoryListCommand;
import com.nus.nexchange.userservice.application.query.OrderHistoryListQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-system/order-histories")
public class OrderHistoryListController {
    private final OrderHistoryListQuery orderHistoryListQuery;

    private final OrderHistoryListCommand orderHistoryListCommand;

    @Autowired
    public OrderHistoryListController(OrderHistoryListQuery orderHistoryListQuery, OrderHistoryListCommand orderHistoryListCommand) {
        this.orderHistoryListQuery = orderHistoryListQuery;
        this.orderHistoryListCommand = orderHistoryListCommand;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<OrderHistoryListDTO> getOrderHistoryList(@PathVariable UUID userId) {
        try {
            OrderHistoryListDTO orderHistoryListDTO = orderHistoryListQuery.getOrderHistoryListByUserId(userId);
            return ResponseEntity.ok(orderHistoryListDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOrderHistoryList(@RequestParam UUID orderHistoryListId, @RequestParam UUID orderHistoryId) {
        try {
            orderHistoryListCommand.removeOrderHistory(orderHistoryId, orderHistoryListId);
//            kafkaProducer.sendDTO("Order History Delete",orderHistoryId);
            return ResponseEntity.ok("Deleted order history");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
