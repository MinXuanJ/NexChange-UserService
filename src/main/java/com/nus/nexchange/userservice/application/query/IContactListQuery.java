package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.ContactListDTO;

import java.util.UUID;

public interface IContactListQuery {
    public ContactListDTO getContactListByUserId(UUID userId);
}
