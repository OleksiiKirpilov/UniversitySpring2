package com.example.s1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Faculty entity. Every faculty is characterized by the name, amount of budget
 * and total places. The amount of budget places must always be less than amount
 * of total places for some faculty.
 */
@Entity
@Data
@Table(name = "faculties")
public class Faculty implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameRu;
    private String nameEn;
    private int budgetPlaces;
    private int totalPlaces;

    public Faculty(String nameRu, String nameEn, int budget, int total) {
        this.nameEn = nameEn;
        this.nameRu = nameRu;
        this.budgetPlaces = budget;
        this.totalPlaces = total;
    }

    public Faculty() {}
}
