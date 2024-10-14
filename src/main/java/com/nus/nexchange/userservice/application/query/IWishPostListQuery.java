package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.WishPostListDTO;

import java.util.UUID;

public interface IWishPostListQuery {
    public WishPostListDTO getWishPostListByUserId(UUID userId);
//    public WishPostListDTO getWishPostListById(UUID wishPostListId);
}
