package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.ContactDTO;
import com.nus.nexchange.userservice.api.dto.ContactListDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import com.nus.nexchange.userservice.domain.entity.UserContact;
import com.nus.nexchange.userservice.infrastructure.repository.ContactListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactListCommand implements IContactListCommand {

    @Autowired
    private ContactListRepository contactListRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void addContact(ContactListDTO contactListDTO, ContactDTO contactDTO) {
        UserContact userContact = modelMapper.map(contactDTO, UserContact.class);
        UserContactList userContactList = modelMapper.map(contactListDTO, UserContactList.class);

        userContactList.createContactInfo(userContact);

        contactListRepository.save(userContactList);
    }

    @Override
    public void updateContact(ContactListDTO contactListDTO, ContactDTO contactDTO) {
        UserContact userContact = modelMapper.map(contactDTO, UserContact.class);
        UserContactList userContactList = modelMapper.map(contactListDTO, UserContactList.class);

        userContactList.updateContactInfo(userContact);

        contactListRepository.save(userContactList);
    }

    @Override
    public void removeContact(UUID contactId, ContactListDTO contactListDTO) {
        UserContactList userContactList = modelMapper.map(contactListDTO, UserContactList.class);

        userContactList.deleteContactInfo(contactId);

        contactListRepository.save(userContactList);
    }
}
