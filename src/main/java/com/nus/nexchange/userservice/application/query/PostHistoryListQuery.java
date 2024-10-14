package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.infrastructure.repository.PostHistoryListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostHistoryListQuery implements IPostHistoryListQuery {
    @Autowired
    private PostHistoryListRepository postHistoryListRepository;

    @Autowired
    private ModelMapper modelMapper;
}
