package com.example.s1.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Subject entity. Every subject is characterized by its name.
 */
@Entity
@Data
@Table(name = "subjects")
public class Subject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nameRu;

    @Column(nullable = false, unique = true)
    private String nameEn;
}
