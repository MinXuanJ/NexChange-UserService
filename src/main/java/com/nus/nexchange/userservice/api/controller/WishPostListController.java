package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.WishPostListDTO;
import com.nus.nexchange.userservice.application.command.ContactListCommand;
import com.nus.nexchange.userservice.application.command.WishPostListCommand;
import com.nus.nexchange.userservice.application.query.ContactListQuery;
import com.nus.nexchange.userservice.application.query.WishPostListQuery;
import com.nus.nexchange.userservice.domain.aggregate.UserWishPostList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-system/wish-posts")
public class WishPostListController {
    @Autowired
    private WishPostListQuery wishPostListQuery;

    @Autowired
    private WishPostListCommand wishPostListCommand;

    @PostMapping
    public ResponseEntity<UserWishPostList> viewWishPostList(@RequestBody WishPostListDTO wishPostListDTO){
        return null;
    }
}
