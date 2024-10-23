package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.Wishposts.WishPostListDTO;

import java.util.UUID;

public interface IWishPostListQuery {
    WishPostListDTO getWishPostListByUserId(UUID userId);

    Boolean comparePostWithWishList(UUID userId, UUID postId);
//    public WishPostListDTO getWishPostListById(UUID wishPostListId);
}
