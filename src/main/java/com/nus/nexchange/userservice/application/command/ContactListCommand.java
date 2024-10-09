package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.ContactDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import com.nus.nexchange.userservice.domain.entity.UserContact;
import com.nus.nexchange.userservice.infrastructure.repository.ContactListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactListCommand implements IContactListCommand{

    @Autowired
    private ContactListRepository contactListRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void addContact(ContactDTO contactDTO) {
        UserContactList contactList = contactListRepository.findByUserId(contactDTO.getUserId());
        if(contactList == null) {
            throw new IllegalArgumentException("UserContactList not found for userId: "+contactDTO.getUserId());
        }

        UserContact userContact = modelMapper.map(contactDTO, UserContact.class);

        contactList.createContactInfo(userContact);

        contactListRepository.save(contactList);
    }

    @Override
    public void updateContact(ContactDTO contactDTO) {
        UserContactList contactList = contactListRepository.findByUserId(contactDTO.getUserId());
        if(contactList == null) {
            throw new IllegalArgumentException("UserContactList not found for userId: "+contactDTO.getUserId());
        }

        UserContact userContact = modelMapper.map(contactDTO, UserContact.class);

        contactList.updateContactInfo(userContact);

        contactListRepository.save(contactList);
    }

    @Override
    public void removeContact(ContactDTO contactDTO) {
        UserContactList contactList = contactListRepository.findByUserId(contactDTO.getUserId());
        if(contactList == null) {
            throw new IllegalArgumentException("UserContactList not found for userId: "+contactDTO.getUserId());
        }

        contactList.deleteContactInfo(contactDTO.getUserId());
    }
}
