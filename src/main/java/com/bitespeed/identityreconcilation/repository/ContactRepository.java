package com.bitespeed.identityreconcilation.repository;

import com.bitespeed.identityreconcilation.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Contact findByEmail(String email);

    List<Contact> findAllByEmail(String email);

    List<Contact> findAllByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    List<Contact> findAllByEmailAndPhoneNumber(String email, String phoneNumber);

    List<Contact> findAll();

    Contact findByPhoneNumber(String phoneNumber);
}
