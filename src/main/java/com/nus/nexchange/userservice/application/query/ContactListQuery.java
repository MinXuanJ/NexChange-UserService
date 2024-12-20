package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.Contacts.ContactDTO;
import com.nus.nexchange.userservice.api.dto.Contacts.ContactListDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import com.nus.nexchange.userservice.infrastructure.repository.ContactListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactListQuery implements IContactListQuery {

    private final ContactListRepository contactListRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ContactListQuery(ContactListRepository contactListRepository, ModelMapper modelMapper) {
        this.contactListRepository = contactListRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ContactListDTO getContactListByUserId(UUID userId) {
        UserContactList userContactList = contactListRepository.findByUserId(userId);

        return getContactListDTO(userContactList);
    }

//    @Override
//    public ContactListDTO getContactListById(UUID contactListId) {
//        UserContactList userContactList = contactListRepository.findById(contactListId).orElse(null);
//
//        return getContactListDTO(userContactList);
//    }

    private ContactListDTO getContactListDTO(UserContactList userContactList) {
        if (userContactList == null) {
            throw new IllegalArgumentException("Contact list not found");
        }

        ContactListDTO contactListDTO = modelMapper.map(userContactList, ContactListDTO.class);

        List<ContactDTO> contactDTOs = userContactList.getUserContacts().stream()
                .map(userContact -> {
                    ContactDTO contactDTO = modelMapper.map(userContact, ContactDTO.class);
                    contactDTO.setContactListId(userContactList.getContactListId());
                    return contactDTO;
                })
                .toList();

        contactListDTO.setUserContacts(contactDTOs);

        return contactListDTO;
    }
}
