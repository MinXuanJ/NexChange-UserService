//package com.nus.nexchange.userservice.application;
//
//import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
//import com.nus.nexchange.userservice.infrastructure.persistence.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class UserDomainService {
//    // 将 Data Model 映射到 Domain Model
////    private UserIdentity mapToDomainModel(UserIdentity entity) {
////        return new UserIdentity(
////                entity.getUserName(),
////                entity.getUserEmail(),
////                entity.getUserPassword()
////        );
////    }
//
//    // 将 Domain Model 映射到 Data Model
////    private UserIdentity mapToDataModel(UserIdentity domain) {
////        return new UserIdentityEntity(
////                domain.getUserName(),
////                domain.getUserEmail(),
////                domain.getUserPassword()
////        );
////    }
//}