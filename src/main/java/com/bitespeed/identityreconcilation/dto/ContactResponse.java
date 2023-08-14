package com.bitespeed.identityreconcilation.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactResponse {

    List<ContactItems> contact;

}
