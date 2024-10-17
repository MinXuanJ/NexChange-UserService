package com.nus.nexchange.userservice.infrastructure.messaging;

import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryDTO;
import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryDTO;
import com.nus.nexchange.userservice.application.command.OrderHistoryListCommand;
import com.nus.nexchange.userservice.application.command.PostHistoryListCommand;
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
        if (orderHistory instanceof OrderHistoryDTO) {
            try {
                orderHistoryListCommand.addOrderHistory((OrderHistoryDTO) orderHistory);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(topics = "updateOrder")
    public void orderUpdateListen(Object orderhistory) {
        if (orderhistory instanceof OrderHistoryDTO) {
            try {
                orderHistoryListCommand.updateOrderHistory((OrderHistoryDTO) orderhistory);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(topics = "newPost")
    public void postPublishListen(Object postHistory) {
        if (postHistory instanceof PostHistoryDTO) {
            try {
                postHistoryListCommand.addPostHistory((PostHistoryDTO) postHistory);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(topics = "updatePost")
    public void postUpdateListen(Object posthistory) {
        if (posthistory instanceof PostHistoryDTO) {
            try {
                postHistoryListCommand.updatePostHistory((PostHistoryDTO) posthistory);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
//Consumer
    //    @Autowired
    //    private DataStorageService dataStorageService;

    //    @KafkaListener(topics = "topic-name"
    //    ,groupId="group-name"
    //    )
    //    public void listen(String message) {
    //        dataStorageService.setData(message);
    //        System.out.println(message);
    //    }

//Controller
    //    @Autowired
    //    private DataStorageService dataStorageService;  // 注入 DataStorageService
    //
    //    @GetMapping("/user")
    //    public UserDTO getUserMessage() {
    //        // 获取存储的对象并进行类型转换
    //        Object data = dataStorageService.getData();
    //        if (data instanceof UserDTO) {
    //            return (UserDTO) data; // 将 Object 转换为 UserDTO 类型
    //        } else {
    //            throw new IllegalArgumentException("Data is not of type UserDTO");
    //        }
    //    }
}
