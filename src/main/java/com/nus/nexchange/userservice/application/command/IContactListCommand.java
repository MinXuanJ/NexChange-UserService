package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.Contacts.ContactDTO;

import java.util.UUID;

public interface IContactListCommand {
    ContactDTO addContact(ContactDTO contactDTO);

    void updateContact(ContactDTO contactDTO);

    void removeContact(UUID contactID, UUID contactListId);
}
