package com.bitespeed.identifyreconcilation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class ContactID implements Serializable {


    private Integer id;
    private String email;
    private String phoneNumber;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
