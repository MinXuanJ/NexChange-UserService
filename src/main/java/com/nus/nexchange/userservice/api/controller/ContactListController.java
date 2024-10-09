package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.ContactDTO;
import com.nus.nexchange.userservice.api.dto.ContactListDTO;
import com.nus.nexchange.userservice.application.command.ContactListCommand;
import com.nus.nexchange.userservice.application.query.ContactListQuery;
import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import org.modelmapper.ModelMapper;
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

    @PostMapping
    public ResponseEntity<ContactListDTO> viewContactList(@RequestBody ContactListDTO contactListDTO) {
        ContactListDTO userContactList = contactListQuery.getContactListByUserId(contactListDTO.getUserId());

        return ResponseEntity.ok(userContactList);
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

    @PostMapping("/update")
    public ResponseEntity<String> updateContact(@RequestBody ContactDTO contactDTO) {
        try{
            contactListCommand.updateContact(contactDTO);
            return ResponseEntity.ok("Updated contact");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteContact(@RequestBody ContactDTO contactDTO) {
        try{
            contactListCommand.removeContact(contactDTO);
            return ResponseEntity.ok("Removed contact");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
