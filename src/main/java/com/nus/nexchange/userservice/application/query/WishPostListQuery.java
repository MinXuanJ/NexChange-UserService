package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.infrastructure.repository.WishPostListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishPostListQuery implements IWishPostListQuery{

    @Autowired
    private WishPostListRepository wishPostListRepository;
}
