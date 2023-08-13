package com.bitespeed.identityreconcilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactItems {

    private int primaryContactId;
    private String emails;
    private String phoneNumbers;
    private int secondaryContactIds;
}
