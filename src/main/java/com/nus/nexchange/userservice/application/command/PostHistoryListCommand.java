package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.domain.aggregate.UserPostHistoryList;
import com.nus.nexchange.userservice.infrastructure.repository.PostHistoryListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostHistoryListCommand implements IPostHistoryListCommand {
    @Autowired
    private PostHistoryListRepository postHistoryListRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void removePostHistory(UUID postHistoryId, UUID postHistoryListId){
        UserPostHistoryList postHistoryList = postHistoryListRepository.findById(postHistoryListId).orElse(null);

        if(postHistoryList == null){
            throw new IllegalArgumentException("PostHistoryList not found");
        }

        postHistoryList.deletePostHistory(postHistoryId);

        postHistoryListRepository.save(postHistoryList);
    }
}
