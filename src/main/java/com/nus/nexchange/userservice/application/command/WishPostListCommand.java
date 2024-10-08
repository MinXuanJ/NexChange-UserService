package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.infrastructure.repository.WishPostListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishPostListCommand implements IWishPostListCommand{

    @Autowired
    private WishPostListRepository wishPostListRepository;
}
