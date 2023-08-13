package com.bitespeed.identityreconcilation.service;

import com.bitespeed.identityreconcilation.dto.ContactItems;
import com.bitespeed.identityreconcilation.model.Contact;
import com.bitespeed.identityreconcilation.model.ContactRequest;
import com.bitespeed.identityreconcilation.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {


    @Autowired
    private ContactRepository contactRepository;


    public ContactItems saveContactResponse(ContactRequest contactRequest) throws Exception {

        ContactItems saveContact = new ContactItems();
        List<Contact> contactList = new ArrayList<>();
        String email = contactRequest.getEmail();
        String phoneNumber = contactRequest.getPhoneNumber();
        boolean isEmailPresent = existingEmail(email);
        boolean isPhonePresent = existingPhoneNumber(phoneNumber);

        if(email != null && phoneNumber != null) {
                saveContact(contactRequest);
                contactList.addAll(findAllByEmail(contactRequest.getEmail()));
                contactList.addAll(findAllByPhoneNUmber(contactRequest.getPhoneNumber()));
                saveContact = showSaveContactResponse(contactList);
        }

        if(email == null) {
            contactList = contactRepository.findAllByPhoneNumber(phoneNumber);
            saveContact = showSavedContactResponse(contactList);
        }

        if(phoneNumber == null) {
            contactList = contactRepository.findAllByEmail(email);
            saveContact = showSavedContactResponse(contactList);
        }

        return saveContact;
    }


    public ContactItems showSavedContactResponse(List<Contact> contactList) {

        ContactItems contactItems = new ContactItems();

        contactItems.setPrimaryContactId(contactList.stream()
                .min(Comparator.comparing(Contact::getId)).get().getId());
        contactItems.setEmails(contactList.stream().map(Contact::getEmail).distinct().collect(Collectors.toList()));
        contactItems.setPhoneNumbers(contactList.stream().map(Contact::getPhoneNumber).distinct().collect(Collectors.toList()));
        contactItems.setSecondaryContactIds(contactList.stream().filter(contact -> contact.getLinkPrecedence() == "Secondary").map(Contact::getId).distinct().collect(Collectors.toList()));
        return contactItems;
    }


    public void saveContact(ContactRequest contactRequest) throws Exception
    {
        saveContactInfo(contactRequest);
    }

    public Contact saveContactInfo(ContactRequest contactRequest) {

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
            saveContactIfExistingEmail(contactRequest);
        }
        else if(!existingEmail(contactRequest.getEmail()) && existingPhoneNumber(contactRequest.getPhoneNumber())) {
            saveContactIfExistingPhoneNumber(contactRequest);
        }
        return saveContact;
    }

    public ContactItems showSaveContactResponse(List<Contact> contactList) {

        ContactItems contactItems = new ContactItems();
        if(contactList.size() == 1) {
            Contact contact = contactList.get(0);
            contactItems.setPrimaryContactId(contact.getId());
            contactItems.setEmails(Arrays.asList(contact.getEmail()));
            contactItems.setPhoneNumbers(Arrays.asList(contact.getPhoneNumber()));
            contactItems.setSecondaryContactIds(new ArrayList<>());
        } else {
            contactItems = showSavedContactResponse(contactList);
        }

        return contactItems;
    }

    public void saveContactIfExistingEmail(ContactRequest contactRequest) {
        Contact saveContact = new Contact();
        List<Contact> existingContact = contactRepository.findAllByEmail(contactRequest.getEmail());
        saveContact.setEmail(contactRequest.getEmail());
        saveContact.setEmail(contactRequest.getEmail());
        saveContact.setPhoneNumber(contactRequest.getPhoneNumber());
        saveContact.setLinkedId(existingContact.stream()
                .min(Comparator.comparing(Contact::getId)).get().getId());
        saveContact.setLinkPrecedence("Secondary");
        saveContact.setCreatedAt(LocalDateTime.now());
        saveContact.setUpdatedAt(LocalDateTime.now());
        saveContact.setDeletedAt(null);

        contactRepository.save(saveContact);

    }

    public void saveContactIfExistingPhoneNumber(ContactRequest contactRequest) {
        Contact saveContact = new Contact();
        List<Contact> existingContact = contactRepository.findAllByPhoneNumber(contactRequest.getPhoneNumber());
        saveContact.setEmail(contactRequest.getEmail());
        saveContact.setPhoneNumber(contactRequest.getPhoneNumber());
        saveContact.setLinkedId(existingContact.stream()
                        .min(Comparator.comparing(Contact::getId)).get().getId());
        saveContact.setLinkPrecedence("Secondary");
        saveContact.setCreatedAt(LocalDateTime.now());
        saveContact.setUpdatedAt(LocalDateTime.now());
        saveContact.setDeletedAt(null);

        contactRepository.save(saveContact);
    }

    public boolean existingEmail(String email) {
        return contactRepository.findByEmail(email) != null;
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

    public List<Contact> findByPhoneNumber(String phoneNumber) {
        return contactRepository.findAllByPhoneNumber(phoneNumber);
    }

    public List<Contact> findAllByEmail(String email) {
        return contactRepository.findAllByEmail(email);
    }

    public List<Contact> findAllByPhoneNUmber(String phoneNumber) {
        return contactRepository.findAllByPhoneNumber(phoneNumber);
    }


    public List<Contact> findAll() {
        return contactRepository.findAll();
    }


}