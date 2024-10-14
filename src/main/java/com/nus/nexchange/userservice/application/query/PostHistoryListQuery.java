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
    @Autowired
    private PostHistoryListRepository postHistoryListRepository;

    @Autowired
    private ModelMapper modelMapper;

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
                .map(postHistoryDTO -> modelMapper.map(postHistoryDTO, PostHistoryDTO.class))
                .toList();

        postHistoryListDTO.setPostHistories(postHistoryDTOS);

        return postHistoryListDTO;
    }
}
