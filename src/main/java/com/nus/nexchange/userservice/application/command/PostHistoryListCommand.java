package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserPostHistoryList;
import com.nus.nexchange.userservice.domain.entity.UserPostHistory;
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
    public void addPostHistory(PostHistoryDTO postHistoryDTO) {
        UserPostHistory userPostHistory = modelMapper.map(postHistoryDTO, UserPostHistory.class);
        UserPostHistoryList userPostHistoryList = postHistoryListRepository.findByUserId(postHistoryDTO.getUserId());

        userPostHistoryList.addPostHistory(userPostHistory);

        postHistoryListRepository.save(userPostHistoryList);
    }

    @Override
    public void updatePostHistory(PostHistoryDTO postHistoryDTO) {
        UserPostHistory userPostHistory = modelMapper.map(postHistoryDTO, UserPostHistory.class);
        UserPostHistoryList userPostHistoryList = postHistoryListRepository.findByUserId(postHistoryDTO.getUserId());

        userPostHistoryList.updatePostHistory(userPostHistory);

        postHistoryListRepository.save(userPostHistoryList);
    }

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
