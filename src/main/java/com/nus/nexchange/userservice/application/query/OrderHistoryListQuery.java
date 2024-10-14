package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryDTO;
import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryListDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserOrderHistoryList;
import com.nus.nexchange.userservice.infrastructure.repository.OrderHistoryListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderHistoryListQuery implements IOrderHistoryListQuery {
    @Autowired
    private OrderHistoryListRepository orderHistoryListRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderHistoryListDTO getOrderHistoryListByUserId(UUID userId) {
        UserOrderHistoryList orderHistoryList = orderHistoryListRepository.findByUserId(userId);

        return getOrderHistoryListDTO(orderHistoryList);
    }

    private OrderHistoryListDTO getOrderHistoryListDTO(UserOrderHistoryList orderHistoryList) {
        if(orderHistoryList == null) {
            throw new IllegalArgumentException("orderHistoryList is null");
        }

        OrderHistoryListDTO orderHistoryListDTO = modelMapper.map(orderHistoryList, OrderHistoryListDTO.class);

        List<OrderHistoryDTO> orderHistoryDTOS = orderHistoryList.getUserOrderHistories().stream()
                .map(userOrderhistory -> modelMapper.map(userOrderhistory, OrderHistoryDTO.class))
                .toList();

        orderHistoryListDTO.setUserOrderHistories(orderHistoryDTOS);

        return orderHistoryListDTO;
    }
}
