package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.entity.UserContact;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserContactList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID contactListId;

    private UUID userId;

    @OneToMany(mappedBy = "userContactList")
    private List<UserContact> userContacts;

    public UserContactList(UUID userId) {
        this.userId = userId;
        userContacts = new ArrayList<>();
    }

    public void addContact(UserContact userContact) {
        userContacts.add(userContact);
        userContact.setUserContactList(this);
    }

    public void updateContact(UserContact userContact) {
        if (userContact == null || userContact.getContactId() == null) {
            throw new IllegalArgumentException("Updated contact or its ID cannot be null");
        }

        UserContact existingContact = userContacts.stream()
                .filter(contact -> contact.getContactId().equals(userContact.getContactId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        existingContact.setContactName(userContact.getContactName());
        existingContact.setContactAddress(userContact.getContactAddress());
        existingContact.setPostalCode(userContact.getPostalCode());
        existingContact.setContactNumber(userContact.getContactNumber());
    }

    public void deleteContact(UUID contactId) {
        UserContact userContactToRemove = userContacts.stream()
                .filter(contact -> contact.getContactId().equals(contactId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        userContacts.remove(userContactToRemove);
        userContactToRemove.setUserContactList(null);
    }
}
