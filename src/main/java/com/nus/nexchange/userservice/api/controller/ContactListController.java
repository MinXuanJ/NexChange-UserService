package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.Contacts.ContactDTO;
import com.nus.nexchange.userservice.api.dto.Contacts.ContactListDTO;
import com.nus.nexchange.userservice.application.command.ContactListCommand;
import com.nus.nexchange.userservice.application.query.ContactListQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-system/contacts")
public class ContactListController {
    @Autowired
    private ContactListQuery contactListQuery;

    @Autowired
    private ContactListCommand contactListCommand;

    @GetMapping("/{userId}")
    public ResponseEntity<ContactListDTO> viewContactList(@PathVariable UUID userId) {
        try {
            ContactListDTO userContactList = contactListQuery.getContactListByUserId(userId);
            return ResponseEntity.ok(userContactList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/new-contact")
    public ResponseEntity<String> addContact(@RequestBody ContactDTO contactDTO) {
        try {
            contactListCommand.addContact(contactDTO);
            return ResponseEntity.ok("Added contact");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping("/contact")
//    public ResponseEntity<ContactDTO> viewContact(@RequestBody ContactDTO contactDTO) {
//
//    }

    @PutMapping("/update")
    public ResponseEntity<String> updateContact(@RequestBody ContactDTO contactDTO) {
        try {
            contactListCommand.updateContact(contactDTO);
            return ResponseEntity.ok("Updated contact");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteContact(@RequestParam UUID contactListId, @RequestParam UUID contactId) {
        try {
            contactListCommand.removeContact(contactId, contactListId);
            return ResponseEntity.ok("Removed contact");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
