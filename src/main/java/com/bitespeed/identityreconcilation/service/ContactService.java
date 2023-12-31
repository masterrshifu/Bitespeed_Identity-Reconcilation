package com.bitespeed.identityreconcilation.service;

import com.bitespeed.identityreconcilation.dto.ContactItems;
import com.bitespeed.identityreconcilation.dto.ContactResponse;
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


    public ContactResponse saveContactResponse(ContactRequest contactRequest) {

        ContactResponse saveContact = new ContactResponse();
        List<Contact> contactList = new ArrayList<>();
        String email = contactRequest.getEmail();
        String phoneNumber = contactRequest.getPhoneNumber();
        boolean isEmailPresent = existingEmail(email);
        boolean isPhoneNumberPresent = existingPhoneNumber(phoneNumber);
        if(email != null && phoneNumber != null) {
            if(isEmailPresent && isPhoneNumberPresent) {
                List<Contact> contactListEmail = findAllByEmail(email);
                if(!contactListEmail.get(0).getPhoneNumber().equals(phoneNumber)) {
                    saveContactIfExistingEmailAndPhoneNumber(contactRequest,phoneNumber);
                    contactList.addAll(findAllByEmail(contactRequest.getEmail()));
                    contactList.addAll(findAllByPhoneNUmber(contactRequest.getPhoneNumber()));
                    contactList = contactList.stream().distinct().collect(Collectors.toList());
                    saveContact = showSaveContactResponse(contactList);
                }

            }
            else {
                saveContact(contactRequest);
                contactList.addAll(findAllByEmail(contactRequest.getEmail()));
                contactList.addAll(findAllByPhoneNUmber(contactRequest.getPhoneNumber()));
                contactList = contactList.stream().distinct().collect(Collectors.toList());
                saveContact = showSaveContactResponse(contactList);
            }
        }
        if(email == null) {
            contactList.addAll(contactRepository.findAllByPhoneNumber(phoneNumber));
            contactList.addAll(contactRepository.findAllByEmail(contactList.get(0).getEmail()));
            saveContact = showSavedContactResponse(contactList);
        }
        if(phoneNumber == null) {
            contactList.addAll(contactRepository.findAllByEmail(email));
            contactList.addAll(contactRepository.findAllByPhoneNumber(contactList.get(0).getPhoneNumber()));
            saveContact = showSavedContactResponse(contactList);
        }


        return saveContact;
    }


    public ContactResponse showSavedContactResponse(List<Contact> contactList) {

        ContactResponse contactResponse = new ContactResponse();
        ContactItems contactItems = new ContactItems();
        List<ContactItems> contactItemsList = new ArrayList<>();
        contactItems.setPrimaryContactId(contactList.stream()
                .min(Comparator.comparing(Contact::getId)).get().getId());
        contactItems.setEmails(contactList.stream().map(Contact::getEmail).distinct().collect(Collectors.toList()));
        contactItems.setPhoneNumbers(contactList.stream().map(Contact::getPhoneNumber).distinct().collect(Collectors.toList()));
        contactItems.setSecondaryContactIds(contactList.stream().filter(contact -> contact.getLinkPrecedence().equals("Secondary")).map(Contact::getId).distinct().collect(Collectors.toList()));
        contactItemsList.add(contactItems);
        contactResponse.setContact(contactItemsList.stream().distinct().collect(Collectors.toList()));
        return contactResponse;
    }


    public void saveContact(ContactRequest contactRequest)
    {
            saveContactInfo(contactRequest);
    }

    public void saveContactInfo(ContactRequest contactRequest) {

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
    }

    public ContactResponse showSaveContactResponse(List<Contact> contactList) {

        ContactResponse contactResponse = new ContactResponse();
        List<ContactItems> contactItemsList = new ArrayList<>();
        ContactItems contactItems = new ContactItems();
        if(contactList.size() == 1) {
            Contact contact = contactList.get(0);
            contactItems.setPrimaryContactId(contact.getId());
            contactItems.setEmails(Arrays.asList(contact.getEmail()));
            contactItems.setPhoneNumbers(Arrays.asList(contact.getPhoneNumber()));
            contactItems.setSecondaryContactIds(new ArrayList<>());
            contactItemsList.add(contactItems);
            contactItemsList.add(contactItems);
            contactResponse.setContact(contactItemsList.stream().distinct().collect(Collectors.toList()));
            return contactResponse;
        } else {
            contactResponse = showSavedContactResponse(contactList);
            return contactResponse;
        }

    }

    public void saveContactIfExistingEmail(ContactRequest contactRequest) {
        Contact saveContact = new Contact();
        List<Contact> existingContact = contactRepository.findAllByEmail(contactRequest.getEmail());
        saveContact.setEmail(contactRequest.getEmail());
        saveContact.setPhoneNumber(contactRequest.getPhoneNumber());
        saveContact.setLinkedId(existingContact.stream()
                .min(Comparator.comparing(Contact::getId)).get().getId());
        saveContact.setLinkPrecedence("Secondary");
        saveContact.setUpdatedAt(LocalDateTime.now());

        contactRepository.save(saveContact);

    }

    public void saveContactIfExistingPhoneNumber(ContactRequest contactRequest) {
        Contact saveContact = new Contact();
        List<Contact> getContactDetails = findAllByPhoneNUmber(contactRequest.getPhoneNumber());
        saveContact.setEmail(contactRequest.getEmail());
        saveContact.setPhoneNumber(contactRequest.getPhoneNumber());
        saveContact.setLinkedId(getContactDetails.get(getContactDetails.size()-1).getId());
        saveContact.setLinkPrecedence("Secondary");
        saveContact.setUpdatedAt(LocalDateTime.now());

        contactRepository.save(saveContact);
    }

    public void saveContactIfExistingEmailAndPhoneNumber(ContactRequest contactRequest, String phoneNumber) {

        Contact saveContact = findByPhoneNumber(phoneNumber);
        List<Contact> getPrimaryContactDetails = findAllByEmail(contactRequest.getEmail());
        saveContact.setLinkedId(getPrimaryContactDetails.get(0).getId());
        saveContact.setLinkPrecedence("Secondary");
        saveContact.setUpdatedAt(LocalDateTime.now());
        contactRepository.save(saveContact);

    }

    public boolean existingEmail(String email) {
        return contactRepository.existsByEmail(email);
    }

    public boolean existingPhoneNumber(String phoneNumber) {
        return contactRepository.existsByPhoneNumber(phoneNumber);
    }

    public Contact findByEmail(String email) {
        return contactRepository.findByEmail(email);
    }

    public Contact findByPhoneNumber(String phoneNumber) {
        return contactRepository.findByPhoneNumber(phoneNumber);
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
