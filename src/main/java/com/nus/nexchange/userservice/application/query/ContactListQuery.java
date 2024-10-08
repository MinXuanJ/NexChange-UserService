package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.infrastructure.repository.ContactListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactListQuery implements IContactListQuery{

    @Autowired
    private ContactListRepository contactListRepository;
}
