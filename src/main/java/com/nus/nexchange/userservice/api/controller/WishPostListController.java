package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.Wishposts.WishPostDTO;
import com.nus.nexchange.userservice.api.dto.Wishposts.WishPostListDTO;
import com.nus.nexchange.userservice.application.command.WishPostListCommand;
import com.nus.nexchange.userservice.application.query.WishPostListQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-system/wish-posts")
public class WishPostListController {
    @Autowired
    private WishPostListQuery wishPostListQuery;

    @Autowired
    private WishPostListCommand wishPostListCommand;

    @GetMapping("/{userId}")
    public ResponseEntity<WishPostListDTO> viewWishPostList(@PathVariable UUID userId) {
        try {
            WishPostListDTO wishPostListDTO = wishPostListQuery.getWishPostListByUserId(userId);
            return ResponseEntity.ok(wishPostListDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/new-wishpost")
    public ResponseEntity<String> addWishPost(@RequestBody WishPostDTO wishPostDTO) {
        try {
            wishPostListCommand.addWishPost(wishPostDTO);
            return ResponseEntity.ok("Added wishpost");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PutMapping("/update")
//    public ResponseEntity<String> updateWishPost(@RequestBody WishPostDTO wishPostDTO) {
//        try {
//            wishPostListCommand.updateWishPost(wishPostDTO);
//            return ResponseEntity.ok("Updated wishpost");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @DeleteMapping
    public ResponseEntity<String> deleteWishPost(@RequestParam UUID wishPostListId, @RequestParam UUID wishPostId) {
        try {
            wishPostListCommand.removeWishPost(wishPostId, wishPostListId);
            return ResponseEntity.ok("Deleted wishpost");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/compare")
    public ResponseEntity<?> compareWishPostList(@RequestParam UUID userId, @RequestParam UUID postId) {
        try{
            Boolean result = wishPostListQuery.comparePostWithWishList(userId, postId);
            return ResponseEntity.ok(result);
        }catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
