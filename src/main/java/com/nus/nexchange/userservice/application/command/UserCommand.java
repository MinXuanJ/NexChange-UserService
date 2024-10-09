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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createUser(UserDTO userDTO) {
        UserIdentity user = modelMapper.map(userDTO, UserIdentity.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));

        userRepository.save(user);

        UserIdentityCreatedEvent event = new UserIdentityCreatedEvent(user.getUserId());
        domainEventPublisher.publish(event);
    }
}
