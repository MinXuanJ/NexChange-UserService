package com.nus.nexchange.userservice.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryDTO;
import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryDTO;
import com.nus.nexchange.userservice.application.command.OrderHistoryListCommand;
import com.nus.nexchange.userservice.application.command.PostHistoryListCommand;
import com.nus.nexchange.userservice.infrastructure.messaging.dto.PostDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @Autowired
    private OrderHistoryListCommand orderHistoryListCommand;

    @Autowired
    private PostHistoryListCommand postHistoryListCommand;

    @KafkaListener(topics = "newOrder")
    public void orderCreateListen(Object orderHistory) {
        try {
            orderHistoryListCommand.addOrderHistory((OrderHistoryDTO) orderHistory);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "updateOrder")
    public void orderUpdateListen(Object orderhistory) {
        try {
            orderHistoryListCommand.updateOrderHistory((OrderHistoryDTO) orderhistory);
        } catch (IllegalArgumentException e) {
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
            System.out.println(postDTOJson);
            PostDTO postDTO = new ObjectMapper().readValue(postDTOJson, PostDTO.class);
            System.out.println(postDTO);
            PostHistoryDTO postHistory = convertToPostHistoryDTO(postDTO);
            System.out.println(postHistory);
            postHistoryListCommand.updatePostHistory(postHistory);
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
}
