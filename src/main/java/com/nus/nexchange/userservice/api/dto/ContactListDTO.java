package com.nus.nexchange.userservice.api.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ContactListDTO {
    private UUID contactListingId;

    private UUID userId;

    private List<ContactDTO> userContacts;
}
