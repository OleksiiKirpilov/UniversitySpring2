package com.example.s1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

//Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authorities {

    @Id
    private String userEmail;

    private String authority;

}
