package com.nus.nexchange.userservice.domain.model;

import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class UserContact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID contactId;

    private String contactName;

    private String contactAddress;

    private String postalCode;

    private String contactNumber;

    @ManyToOne
    @JoinColumn(name = "contact_listing_id", nullable = false)
    private UserContactList userContactList;

    public UserContact(String contactName, String contactAddress, String postalCode, String contactNumber) {
        this.contactName = contactName;
        this.contactAddress = contactAddress;
        this.postalCode = postalCode;
        this.contactNumber = contactNumber;
    }
}
