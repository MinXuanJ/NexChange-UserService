package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.Contacts.ContactDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import com.nus.nexchange.userservice.domain.entity.UserContact;
import com.nus.nexchange.userservice.infrastructure.repository.ContactListRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactListCommand implements IContactListCommand {

    private final ContactListRepository contactListRepository;

    private final ModelMapper modelMapper;

    private final EntityManager entityManager;

    @Autowired
    public ContactListCommand(ContactListRepository contactListRepository, ModelMapper modelMapper, EntityManager entityManager) {
        this.contactListRepository = contactListRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public ContactDTO addContact(ContactDTO contactDTO) {
        UserContact userContact = modelMapper.map(contactDTO, UserContact.class);
        userContact.setContactId(UUID.randomUUID());
        UserContactList userContactList = contactListRepository.findById(contactDTO.getContactListId()).orElse(null);

        if (userContactList == null) {
            throw new IllegalArgumentException("Contact list does not exist");
        }

        userContactList.addContact(userContact);

        contactListRepository.save(userContactList);

        return modelMapper.map(userContact, ContactDTO.class);
    }

    @Override
    public void updateContact(ContactDTO contactDTO) {
        UserContact userContact = modelMapper.map(contactDTO, UserContact.class);
        UserContactList userContactList = contactListRepository.findById(contactDTO.getContactListId()).orElse(null);

        if (userContactList == null) {
            throw new IllegalArgumentException("Contact list does not exist");
        }

        userContactList.updateContact(userContact);

        contactListRepository.save(userContactList);
    }

    @Override
    public void removeContact(UUID contactId, UUID contactListId) {
        UserContactList userContactList = contactListRepository.findById(contactListId).orElse(null);

        if (userContactList == null) {
            throw new IllegalArgumentException("Contact list does not exist");
        }

        userContactList.deleteContact(contactId);

        contactListRepository.save(userContactList);
    }
}
