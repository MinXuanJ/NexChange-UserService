package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.Wishposts.WishPostDTO;
import com.nus.nexchange.userservice.api.dto.Wishposts.WishPostListDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserWishPostList;
import com.nus.nexchange.userservice.infrastructure.repository.WishPostListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishPostListQuery implements IWishPostListQuery {

    @Autowired
    private WishPostListRepository wishPostListRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public WishPostListDTO getWishPostListByUserId(UUID userId) {
        UserWishPostList wishPostList = wishPostListRepository.findByUserId(userId);
        return getWishPostListDTO(wishPostList);
    }

//    @Override
//    public WishPostListDTO getWishPostListById(UUID wishPostListId) {
//        UserWishPostList wishPostList = wishPostListRepository.findById(wishPostListId).orElse(null);
//        return getWishPostListDTO(wishPostList);
//    }

    private WishPostListDTO getWishPostListDTO(UserWishPostList userWishPostList) {
        if (userWishPostList == null) {
            throw new IllegalArgumentException("Wishpost list not found");
        }

        WishPostListDTO wishPostListDTO = modelMapper.map(userWishPostList, WishPostListDTO.class);

        List<WishPostDTO> wishPostDTOs = userWishPostList.getWishPosts().stream()
                .map(userWishPost -> modelMapper.map(userWishPost, WishPostDTO.class))
                .toList();

        wishPostListDTO.setWishPosts(wishPostDTOs);

        return wishPostListDTO;
    }
}
