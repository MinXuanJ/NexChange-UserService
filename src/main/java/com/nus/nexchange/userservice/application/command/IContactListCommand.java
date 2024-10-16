package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.Contacts.ContactDTO;

import java.util.UUID;

public interface IContactListCommand {
    public void addContact(ContactDTO contactDTO);

    public void updateContact(ContactDTO contactDTO);

    public void removeContact(UUID contactID, UUID contactListId);
}
