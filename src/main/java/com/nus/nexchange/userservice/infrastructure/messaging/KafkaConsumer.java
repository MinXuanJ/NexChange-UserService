package com.nus.nexchange.userservice.infrastructure.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    private DataStorageService dataStorageService;

    @KafkaListener(topics = "topic-name"
//    ,groupId="group-name"
    )
    public void listen(String message) {
        dataStorageService.setData(message);
        System.out.println(message);
    }


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
