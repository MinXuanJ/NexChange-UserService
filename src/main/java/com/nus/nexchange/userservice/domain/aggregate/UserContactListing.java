package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.model.UserContact;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserContactListing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID contactListingId;

    private UUID userId;

    @OneToMany(mappedBy = "userContactList")
    private List<UserContact> userContacts;

    public void createContactInfo(UserContact userContact) {
        userContacts.add(userContact);
        userContact.setUserContactList(this);
    }

    public void updateContactInfo(UserContact userContact) {
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

    public void deleteContactInfo(UserContact userContact) {
        userContacts.remove(userContact);
        userContact.setUserContactList(null);
    }
//
//    public void displayAllContactInfo(){
//
//    }
}
