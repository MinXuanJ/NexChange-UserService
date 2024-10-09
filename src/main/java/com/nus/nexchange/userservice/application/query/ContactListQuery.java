package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.ContactDTO;
import com.nus.nexchange.userservice.api.dto.ContactListDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import com.nus.nexchange.userservice.infrastructure.repository.ContactListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactListQuery implements IContactListQuery{

    @Autowired
    private ContactListRepository contactListRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ContactListDTO getContactListByUserId(UUID userId) {
        UserContactList userContactList = contactListRepository.findByUserId(userId);

        ContactListDTO contactListDTO = modelMapper.map(userContactList, ContactListDTO.class);

        List<ContactDTO> contactDTOs = userContactList.getUserContacts().stream()
                .map(userContact -> modelMapper.map(userContact,ContactDTO.class))
                .toList();

        contactListDTO.setUserContacts(contactDTOs);

        return contactListDTO;
    }
}
