package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.UserDTO;
import com.nus.nexchange.userservice.application.event.DomainEventPublisher;
import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import com.nus.nexchange.userservice.domain.event.UserIdentityCreatedEvent;
import com.nus.nexchange.userservice.infrastructure.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCommand implements IUserCommand {

    private final UserRepository userRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final ModelMapper modelMapper;

    @Autowired
    public UserCommand(UserRepository userRepository, DomainEventPublisher domainEventPublisher, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createUser(UserDTO userDTO) {
        UserIdentity user = modelMapper.map(userDTO, UserIdentity.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));

        userRepository.save(user);

        UserIdentityCreatedEvent event = new UserIdentityCreatedEvent(user.getUserId(), user.getUserName(), userDTO.getAvatarURL());
        domainEventPublisher.publish(event);
    }

    @Override
    public void resetPassword(UserDTO userDTO) {
        UserIdentity user = userRepository.findById(userDTO.getUserId()).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));

        userRepository.save(user);
    }
}
