package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserOrderHistoryList;
import com.nus.nexchange.userservice.domain.entity.OrderStatus;
import com.nus.nexchange.userservice.domain.entity.UserOrderHistory;
import com.nus.nexchange.userservice.infrastructure.repository.OrderHistoryListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderHistoryListCommand implements IOrderHistoryListCommand {
    
    private final OrderHistoryListRepository orderHistoryListRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public OrderHistoryListCommand(OrderHistoryListRepository orderHistoryListRepository, ModelMapper modelMapper) {
        this.orderHistoryListRepository = orderHistoryListRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addOrderHistory(OrderHistoryDTO orderHistoryDTO) {
        UserOrderHistory userOrderHistory = modelMapper.map(orderHistoryDTO, UserOrderHistory.class);
        UserOrderHistoryList userOrderHistoryList = orderHistoryListRepository.findByUserId(orderHistoryDTO.getUserId());

        if (userOrderHistoryList == null) {
            throw new IllegalArgumentException("Order history list not found");
        }

        userOrderHistoryList.addUserOrderHistory(userOrderHistory);

        orderHistoryListRepository.save(userOrderHistoryList);
    }

//    @Override
//    public void updateOrderHistory(OrderHistoryDTO orderHistoryDTO) {
//        UserOrderHistory userOrderHistory = modelMapper.map(orderHistoryDTO, UserOrderHistory.class);
//        UserOrderHistoryList userOrderHistoryList = orderHistoryListRepository.findByUserId(orderHistoryDTO.getUserId());
//
//        if (userOrderHistoryList == null) {
//            throw new IllegalArgumentException("Order history list not found");
//        }
//
//        userOrderHistoryList.updateUserOrderHistory(userOrderHistory);
//
//        orderHistoryListRepository.save(userOrderHistoryList);
//    }

    @Override
    public void updateOrderHistoryStatus(UUID userId, UUID orderId, OrderStatus orderStatus) {
        UserOrderHistoryList userOrderHistoryList = orderHistoryListRepository.findByUserId(userId);

        if (userOrderHistoryList == null) {
            throw new IllegalArgumentException("Order history list not found");
        }

        userOrderHistoryList.updateUserOrderHistoryStatus(orderId, orderStatus);

        orderHistoryListRepository.save(userOrderHistoryList);
    }

    @Override
    public void removeOrderHistory(UUID orderHistoryId, UUID orderHistoryListId) {
        UserOrderHistoryList orderHistoryList = orderHistoryListRepository.findById(orderHistoryListId).orElse(null);
        if (orderHistoryList == null) {
            throw new IllegalArgumentException("OrderHistoryList not found");
        }

        //publish an event to kafka to let order delete the order

        orderHistoryList.deleteOrderHistory(orderHistoryId);

        orderHistoryListRepository.save(orderHistoryList);
    }
}
