package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.Wishposts.WishPostDTO;

import java.util.UUID;

public interface IWishPostListCommand {
    public void addWishPost(WishPostDTO wishPostDTO);

//    public void updateWishPost(WishPostDTO wishPostDTO);

    public void removeWishPost(UUID wishPostId, UUID wishPostListId);
}
