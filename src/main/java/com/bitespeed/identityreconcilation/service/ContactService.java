package com.bitespeed.identityreconcilation.service;

import com.bitespeed.identityreconcilation.exception.ContactError;
import com.bitespeed.identityreconcilation.model.Contact;
import com.bitespeed.identityreconcilation.model.ContactRequest;
import com.bitespeed.identityreconcilation.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {


    @Autowired
    private ContactRepository contactRepository;


    public Contact saveContact(ContactRequest contactRequest) throws Exception
    {
        return saveContactInfo(contactRequest);
    }

    public Contact saveContactInfo(ContactRequest contactRequest) throws ContactError {

        Contact saveContact = new Contact();
        if(!existingEmail(contactRequest.getEmail()) && !existingPhoneNumber(contactRequest.getPhoneNumber())) {
            saveContact.setEmail(contactRequest.getEmail());
            saveContact.setPhoneNumber(contactRequest.getPhoneNumber());
            saveContact.setLinkPrecedence("Primary");
            saveContact.setCreatedAt(LocalDateTime.now());
            saveContact.setUpdatedAt(LocalDateTime.now());
            saveContact.setDeletedAt(null);
            saveContact.setLinkedId(null);

            contactRepository.save(saveContact);
        }

        else if(existingEmail(contactRequest.getEmail()) && !existingPhoneNumber(contactRequest.getPhoneNumber())) {
            saveContact = saveContactIfExistingEmail(contactRequest);
        }
        else if(!existingEmail(contactRequest.getEmail()) && existingPhoneNumber(contactRequest.getPhoneNumber())) {
            saveContact = saveContactIfExistingPhoneNumber(contactRequest);
        }
        else {
            throw new ContactError("Contact Already Exists with Email or Phone");
        }

        return saveContact;
    }

    public Contact saveContactIfExistingEmail(ContactRequest contactRequest) {
        Contact saveContact = new Contact();
        Contact existingContact = contactRepository.findByEmail(contactRequest.getEmail());
        saveContact.setEmail(contactRequest.getEmail());
        saveContact.setPhoneNumber(contactRequest.getPhoneNumber());
        saveContact.setLinkedId(existingContact.getId());
        saveContact.setLinkPrecedence("Secondary");
        saveContact.setCreatedAt(LocalDateTime.now());
        saveContact.setUpdatedAt(LocalDateTime.now());
        saveContact.setDeletedAt(null);

        contactRepository.save(saveContact);

        return saveContact;
    }

    public Contact saveContactIfExistingPhoneNumber(ContactRequest contactRequest) {
        Contact saveContact = new Contact();
        Contact existingContact = contactRepository.findByPhoneNumber(contactRequest.getPhoneNumber());
        saveContact.setEmail(contactRequest.getEmail());
        saveContact.setPhoneNumber(contactRequest.getPhoneNumber());
        saveContact.setLinkedId(existingContact.getId());
        saveContact.setLinkPrecedence("Secondary");
        saveContact.setCreatedAt(LocalDateTime.now());
        saveContact.setUpdatedAt(LocalDateTime.now());
        saveContact.setDeletedAt(null);

        contactRepository.save(saveContact);

        return saveContact;
    }

    public boolean existingEmail(String email) {

        if(contactRepository.findByEmail(email) != null) {
            return true;
        }
        return false;
    }

    public boolean existingPhoneNumber(String phoneNumber) {
        return contactRepository.existsByPhoneNumber(phoneNumber);
    }

    public Contact findByEmail(ContactRequest contactRequest) {
        return contactRepository.findByEmail(contactRequest.getEmail());
    }

    public Contact findByEmail(String email) {
        return contactRepository.findByEmail(email);
    }

    public Contact findByPhoneNumber(ContactRequest contactRequest) {
        return contactRepository.findByPhoneNumber(contactRequest.getPhoneNumber());
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }


}
