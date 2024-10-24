package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.Wishposts.WishPostDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserWishPostList;
import com.nus.nexchange.userservice.domain.entity.UserWishPost;
import com.nus.nexchange.userservice.infrastructure.repository.WishPostListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WishPostListCommand implements IWishPostListCommand {

    private final WishPostListRepository wishPostListRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public WishPostListCommand(WishPostListRepository wishPostListRepository, ModelMapper modelMapper) {
        this.wishPostListRepository = wishPostListRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addWishPost(WishPostDTO wishPostDTO) {
        UserWishPost wishPost = modelMapper.map(wishPostDTO, UserWishPost.class);
        UserWishPostList wishPostList = wishPostListRepository.findByUserId(wishPostDTO.getUserId());
        if (wishPostList == null) {
            throw new IllegalArgumentException("WishPostList not found");
        }

        wishPostList.addUserWishPost(wishPost);

        wishPostListRepository.save(wishPostList);
    }

//    @Override
//    public void updateWishPost(WishPostDTO wishPostDTO) {
//        UserWishPost wishPost = modelMapper.map(wishPostDTO, UserWishPost.class);
//        UserWishPostList wishPostList = wishPostListRepository.findById(wishPostDTO.getWishPostListId()).orElse(null);
//        if (wishPostList == null) {
//            throw new IllegalArgumentException("WishPostList not found");
//        }
//
//        wishPostList.updateUserWishPost(wishPost);
//
//        wishPostListRepository.save(wishPostList);
//    }

    @Override
    public void removeWishPost(UUID wishPostId, UUID userId) {
        UserWishPostList wishPostList = wishPostListRepository.findByUserId(userId);
        if (wishPostList == null) {
            throw new IllegalArgumentException("WishPostList not found");
        }

        wishPostList.deleteUserWishPost(wishPostId);

        wishPostListRepository.save(wishPostList);
    }
}
