package com.nus.nexchange.userservice.api.dto.Contacts;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ContactListDTO {
    private UUID contactListId;

    private UUID userId;

    private List<ContactDTO> userContacts;
}
