package com.nus.nexchange.userservice.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ContactDTO {
    private UUID contactListId;

    private UUID contactId;

    private String contactName;

    private String contactAddress;

    private String postalCode;

    private String contactNumber;

    private boolean defaultContact;
}
