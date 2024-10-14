package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryListDTO;
import com.nus.nexchange.userservice.application.command.PostHistoryListCommand;
import com.nus.nexchange.userservice.application.query.PostHistoryListQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-system/post-histories")
public class PostHistoryListController {
    @Autowired
    private PostHistoryListQuery postHistoryListQuery;

    @Autowired
    private PostHistoryListCommand postHistoryListCommand;

    @GetMapping("/{userId}")
    public ResponseEntity<PostHistoryListDTO> getPostHistoryList(@PathVariable UUID userId) {
        return ResponseEntity.notFound().build();
    }
}
