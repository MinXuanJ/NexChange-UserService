package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.ContactDTO;
import com.nus.nexchange.userservice.api.dto.ContactListDTO;

import java.util.UUID;

public interface IContactListCommand {
    public void addContact(ContactDTO contactDTO);

    public void updateContact(ContactDTO contactDTO);

    public void removeContact(UUID contactID, UUID contactListId);
}
