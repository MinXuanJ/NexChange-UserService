package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.application.command.ContactListCommand;
import com.nus.nexchange.userservice.application.query.ContactListQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-system/contacts")
public class ContactListController {
    @Autowired
    private ContactListQuery contactListQuery;

    @Autowired
    private ContactListCommand contactListCommand;
}
