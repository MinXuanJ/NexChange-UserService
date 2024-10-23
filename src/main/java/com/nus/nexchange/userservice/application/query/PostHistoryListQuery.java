package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryDTO;
import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryListDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserPostHistoryList;
import com.nus.nexchange.userservice.infrastructure.repository.PostHistoryListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostHistoryListQuery implements IPostHistoryListQuery {
    private final PostHistoryListRepository postHistoryListRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public PostHistoryListQuery(PostHistoryListRepository postHistoryListRepository, ModelMapper modelMapper) {
        this.postHistoryListRepository = postHistoryListRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostHistoryListDTO getPostHistoryListByUserId(UUID userId) {
        UserPostHistoryList postHistoryList = postHistoryListRepository.findByUserId(userId);
        return getPostHistoryListDTO(postHistoryList);
    }

    private PostHistoryListDTO getPostHistoryListDTO(UserPostHistoryList postHistoryList) {
        if (postHistoryList == null) {
            throw new IllegalArgumentException("postHistoryList is null");
        }

        PostHistoryListDTO postHistoryListDTO = modelMapper.map(postHistoryList, PostHistoryListDTO.class);

        List<PostHistoryDTO> postHistoryDTOS = postHistoryList.getUserPostHistories().stream()
                .map(userPostHistory -> {
                    PostHistoryDTO postHistoryDTO = modelMapper.map(userPostHistory, PostHistoryDTO.class);
                    postHistoryDTO.setPostHistoryListId(postHistoryList.getPostHistoryListId());
                    return postHistoryDTO;
                })
                .toList();

        postHistoryListDTO.setPostHistories(postHistoryDTOS);

        return postHistoryListDTO;
    }
}
