package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.Wishposts.WishPostListDTO;

import java.util.UUID;

public interface IWishPostListQuery {
    public WishPostListDTO getWishPostListByUserId(UUID userId);
    public Boolean comparePostWithWishList(UUID userId,UUID postId);
//    public WishPostListDTO getWishPostListById(UUID wishPostListId);
}
