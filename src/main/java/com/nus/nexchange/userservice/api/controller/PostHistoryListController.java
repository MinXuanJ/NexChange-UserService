package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryListDTO;
import com.nus.nexchange.userservice.application.query.PostHistoryListQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-system/post-histories")
public class PostHistoryListController {
    private final PostHistoryListQuery postHistoryListQuery;

    @Autowired
    public PostHistoryListController(PostHistoryListQuery postHistoryListQuery) {
        this.postHistoryListQuery = postHistoryListQuery;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PostHistoryListDTO> getPostHistoryList(@PathVariable UUID userId) {
        try {
            PostHistoryListDTO postHistoryListDTO = postHistoryListQuery.getPostHistoryListByUserId(userId);
            return ResponseEntity.ok(postHistoryListDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
