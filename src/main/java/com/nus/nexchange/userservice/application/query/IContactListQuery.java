package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.Contacts.ContactListDTO;

import java.util.UUID;

public interface IContactListQuery {
    ContactListDTO getContactListByUserId(UUID userId);
//    public ContactListDTO getContactListById(UUID contactListId);
}
