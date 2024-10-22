package com.nus.nexchange.userservice.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nus.nexchange.userservice.api.dto.Contacts.ContactDTO;
import com.nus.nexchange.userservice.api.dto.Contacts.ContactListDTO;
import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryDTO;
import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryDTO;
import com.nus.nexchange.userservice.application.command.OrderHistoryListCommand;
import com.nus.nexchange.userservice.application.command.PostHistoryListCommand;
import com.nus.nexchange.userservice.application.query.ContactListQuery;
import com.nus.nexchange.userservice.domain.entity.OrderStatus;
import com.nus.nexchange.userservice.infrastructure.messaging.dto.UUIDOrderDTO;
import com.nus.nexchange.userservice.infrastructure.messaging.dto.OrderContactDTO;
import com.nus.nexchange.userservice.infrastructure.messaging.dto.OrderDTO;
import com.nus.nexchange.userservice.infrastructure.messaging.dto.PostDTO;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaConsumer {
    @Autowired
    private OrderHistoryListCommand orderHistoryListCommand;

    @Autowired
    private PostHistoryListCommand postHistoryListCommand;

    @Autowired
    private ContactListQuery contactListQuery;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ModelMapper modelMapper;

    @KafkaListener(topics = "CreateOrder")
    @Transactional
    public void orderCreateListen(String createOrderDTOJson) {
        try {
            UUIDOrderDTO UUIDOrderDTO = new ObjectMapper().readValue(createOrderDTOJson, UUIDOrderDTO.class);
            ContactListDTO contactListDTO = contactListQuery.getContactListByUserId(UUIDOrderDTO.getUserId());

            ContactDTO contactDTO = contactListDTO.getUserContacts().stream()
                    .filter(ContactDTO::isDefaultContact)
                    .findFirst().orElse(null);
            OrderContactDTO orderContactDTO = modelMapper.map(contactDTO, OrderContactDTO.class);
            orderContactDTO.setUserId(UUIDOrderDTO.getUserId());
            String orderContactDTOJson = new ObjectMapper().writeValueAsString(orderContactDTO);

//            orderContactDTO.setOrderId(UUIDOrderDTO.getOrderId());
            orderContactDTO.setSecret(UUIDOrderDTO.getSecret());

            kafkaProducer.sendMessage("OrderBuyer", orderContactDTOJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "CreatedOrder")
    @Transactional
    public void orderCreatedListen(String orderDTOJson) {
        try {
            OrderDTO orderDTO = new ObjectMapper().readValue(orderDTOJson, OrderDTO.class);
            OrderHistoryDTO orderHistoryDTO = convertToOrderHistoryDTO(orderDTO);

            orderHistoryListCommand.addOrderHistory(orderHistoryDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "CancelOrder")
    @Transactional
    public void orderCancelListen(String orderDTOJson) {
        try {
            UUIDOrderDTO UUIDOrderDTO = new ObjectMapper().readValue(orderDTOJson, UUIDOrderDTO.class);
            orderHistoryListCommand.updateOrderHistoryStatus(UUIDOrderDTO.getUserId(), UUIDOrderDTO.getOrderId(), OrderStatus.CANCELED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "ExpireOrder")
    @Transactional
    public void orderExpireListen(String orderDTOJson) {
        try {
            UUIDOrderDTO UUIDOrderDTO = new ObjectMapper().readValue(orderDTOJson, UUIDOrderDTO.class);
            orderHistoryListCommand.updateOrderHistoryStatus(UUIDOrderDTO.getUserId(), UUIDOrderDTO.getOrderId(), OrderStatus.EXPIRED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "newPost")
    @Transactional
    public void postPublishListen(String postDTOJson) {
        try {
            PostDTO postDTO = new ObjectMapper().readValue(postDTOJson, PostDTO.class);
            PostHistoryDTO postHistory = convertToPostHistoryDTO(postDTO);
            postHistoryListCommand.addPostHistory(postHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "updatePost")
    @Transactional
    public void postUpdateListen(String postDTOJson) {
        try {
            PostDTO postDTO = new ObjectMapper().readValue(postDTOJson, PostDTO.class);
            PostHistoryDTO postHistory = convertToPostHistoryDTO(postDTO);
            postHistoryListCommand.updatePostHistory(postHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "deletePost")
    @Transactional
    public void deletePostListen(String postDTOJson) {
        try {
            PostDTO postDTO = new ObjectMapper().readValue(postDTOJson, PostDTO.class);
            UUID userId = postDTO.getUserId();
            UUID postId = postDTO.getPostId();
            postHistoryListCommand.removePostHistory(postId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OrderHistoryDTO convertToOrderHistoryDTO(PostDTO postDTO) {
        return null;
    }

    private PostHistoryDTO convertToPostHistoryDTO(PostDTO postDTO) {
        PostHistoryDTO postHistoryDTO = new PostHistoryDTO();
        postHistoryDTO.setUserId(postDTO.getUserId());
        postHistoryDTO.setRefPostId(postDTO.getPostId());
        postHistoryDTO.setRefPostTitle(postDTO.getPostTittle());
        postHistoryDTO.setRefPostShortCutURL(postDTO.getPostShortcutURL());
        postHistoryDTO.setRefPostStatus(postDTO.getPostStatus());

        return postHistoryDTO;
    }

    private OrderHistoryDTO convertToOrderHistoryDTO(OrderDTO orderDTO) {
        OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO();

        orderHistoryDTO.setUserId(orderDTO.getUserId());
        orderHistoryDTO.setRefOrderId(orderDTO.getOrderId());
        orderHistoryDTO.setRefOrderTitle(orderDTO.getRefPostTitle());
        orderHistoryDTO.setRefOrderShoutCutURL(orderDTO.getRefPostShortcutURL());
        orderHistoryDTO.setRefOrderPrice(orderDTO.getRefPostPrice());
        orderHistoryDTO.setRefOrderStatus(orderDTO.getOrderStatus());

        return orderHistoryDTO;
    }
}
