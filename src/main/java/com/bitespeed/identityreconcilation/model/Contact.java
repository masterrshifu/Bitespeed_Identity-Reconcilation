package com.bitespeed.identityreconcilation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_contact")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ContactID.class)
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Id @Column(name = "email",nullable = false)
    private String email;

    @Id @Column(name = "phoneNumber",nullable = false)
    private String phoneNumber;

    @Column(name = "linkedID")
    private Integer linkedId;

    @Column(name = "linkedPrecedence")
    private String linkPrecedence;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;


}
