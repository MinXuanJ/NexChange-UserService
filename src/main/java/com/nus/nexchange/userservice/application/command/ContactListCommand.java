package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.infrastructure.repository.ContactListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactListCommand implements IContactListCommand{

    @Autowired
    private ContactListRepository contactListRepository;
}
