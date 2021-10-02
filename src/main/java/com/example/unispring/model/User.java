package com.example.unispring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User entity. Has id, first and last names, email, password,
 * role and preferred language. Email should be unique.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String lang;


    public User(String email, String password, String firstName, String lastName, String role, String lang) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", password=***"
                + ", firstName=" + firstName + ", lastName=" + lastName
                + ", role=" + role + ", lang=" + lang + "]";
    }
}

