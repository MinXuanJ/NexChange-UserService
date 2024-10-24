package com.nus.nexchange.userservice.domain.entity;

import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Data
@NoArgsConstructor
public class UserContact {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID contactId;

    private String contactName;

    private String contactAddress;

    private String postalCode;

    private String contactNumber;

    private boolean defaultContact;

    @ManyToOne
    @JoinColumn(name = "contact_list_id", nullable = false)
    private UserContactList userContactList;
}
