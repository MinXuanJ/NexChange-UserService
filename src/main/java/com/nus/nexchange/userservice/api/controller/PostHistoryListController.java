package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryListDTO;
import com.nus.nexchange.userservice.application.command.PostHistoryListCommand;
import com.nus.nexchange.userservice.application.query.PostHistoryListQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        try {
            PostHistoryListDTO postHistoryListDTO = postHistoryListQuery.getPostHistoryListByUserId(userId);
            return ResponseEntity.ok(postHistoryListDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping("/new-post-history")
//    public ResponseEntity<String> addPostHistory(@RequestBody PostHistoryListDTO postHistoryListDTO) {
//
//    }
//
//    @PutMapping("/update")
//    public ResponseEntity<String> updatePostHistory(@RequestBody PostHistoryListDTO postHistoryListDTO) {
//
//    }

    @DeleteMapping
    public ResponseEntity<String> deletePostHistoryList(@RequestParam UUID postHistoryListId, @RequestParam UUID postHistoryId) {
        try {
            postHistoryListCommand.removePostHistory(postHistoryId, postHistoryListId);
            return ResponseEntity.ok("Deleted post history");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
