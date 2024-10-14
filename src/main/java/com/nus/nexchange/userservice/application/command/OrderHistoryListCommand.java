package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.application.query.OrderHistoryListQuery;
import com.nus.nexchange.userservice.domain.aggregate.UserOrderHistoryList;
import com.nus.nexchange.userservice.infrastructure.repository.OrderHistoryListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderHistoryListCommand implements IOrderHistoryListCommand {
    @Autowired
    private OrderHistoryListRepository orderHistoryListRepository;

    @Autowired
    private ModelMapper modelMapper;

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
