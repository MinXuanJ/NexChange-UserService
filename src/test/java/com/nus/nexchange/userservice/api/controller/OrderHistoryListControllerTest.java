package com.nus.nexchange.userservice.api.controller;


import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryListDTO;
import com.nus.nexchange.userservice.application.command.OrderHistoryListCommand;
import com.nus.nexchange.userservice.application.query.OrderHistoryListQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class OrderHistoryListControllerTest {
    @InjectMocks
    private OrderHistoryListController orderHistoryListController;

    @Mock
    private OrderHistoryListQuery orderHistoryListQuery;

    @Mock
    private OrderHistoryListCommand orderHistoryListCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderHistoryList_Success() {
        UUID userId = UUID.randomUUID();
        OrderHistoryListDTO mockDTO = new OrderHistoryListDTO(); // 假设你有一个默认构造函数

        when(orderHistoryListQuery.getOrderHistoryListByUserId(userId)).thenReturn(mockDTO);

        ResponseEntity<OrderHistoryListDTO> response = orderHistoryListController.getOrderHistoryList(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockDTO, response.getBody());
    }

    @Test
    public void testGetOrderHistoryList_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(orderHistoryListQuery.getOrderHistoryListByUserId(userId)).thenThrow(new IllegalArgumentException("User not found"));

        ResponseEntity<OrderHistoryListDTO> response = orderHistoryListController.getOrderHistoryList(userId);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteOrderHistoryList_Success() {
        UUID orderHistoryListId = UUID.randomUUID();
        UUID orderHistoryId = UUID.randomUUID();

        ResponseEntity<String> response = orderHistoryListController.deleteOrderHistoryList(orderHistoryListId, orderHistoryId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted order history", response.getBody());
    }

    @Test
    public void testDeleteOrderHistoryList_BadRequest() {
        UUID orderHistoryListId = UUID.randomUUID();
        UUID orderHistoryId = UUID.randomUUID();

        doThrow(new IllegalArgumentException("Invalid request")).when(orderHistoryListCommand).removeOrderHistory(orderHistoryId, orderHistoryListId);

        ResponseEntity<String> response = orderHistoryListController.deleteOrderHistoryList(orderHistoryListId, orderHistoryId);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid request", response.getBody());
    }
}
