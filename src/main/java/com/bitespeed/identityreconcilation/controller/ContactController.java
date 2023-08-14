package com.bitespeed.identityreconcilation.controller;

import com.bitespeed.identityreconcilation.dto.ContactItems;
import com.bitespeed.identityreconcilation.dto.ContactResponse;
import com.bitespeed.identityreconcilation.model.Contact;
import com.bitespeed.identityreconcilation.model.ContactRequest;
import com.bitespeed.identityreconcilation.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContactController {


    @Autowired
    private ContactService contactService;

    @PostMapping("/identify")
    public ResponseEntity<ContactResponse> saveUser(@RequestBody ContactRequest contactRequest) throws Exception {
        try {
            ContactResponse saveContact = contactService.saveContactResponse(contactRequest);
            return new ResponseEntity<>(saveContact, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<Contact> getContactByEmailId(@PathVariable String email) {
        Contact contact = contactService.findByEmail(email);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contactList = contactService.findAll();
        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }
}
