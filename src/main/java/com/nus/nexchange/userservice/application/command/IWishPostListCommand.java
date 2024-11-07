package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.Wishposts.WishPostDTO;

import java.util.UUID;

public interface IWishPostListCommand {
    void addWishPost(WishPostDTO wishPostDTO);

//    public void updateWishPost(WishPostDTO wishPostDTO);

    void removeWishPost(UUID wishPostId, UUID userId);
}
