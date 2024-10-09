package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.UserDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import com.nus.nexchange.userservice.infrastructure.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserQuery implements IUserQuery {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserIdentity> users = userRepository.findAll();

        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }

    @Override
    public UserDTO getUserById(UUID id) {
        UserIdentity user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return userDTO;
    }
}
