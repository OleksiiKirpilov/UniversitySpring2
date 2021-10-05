package com.example.unispring.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Faculty entity. It has a name, amount of budget and total places.
 * The amount of budget places must always be less than amount
 * of total places.
 */
@Entity
@Data
@Table(name = "faculties")
public class Faculty implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nameRu;

    @Column
    private String nameEn;

    @Column
    private int budgetPlaces;

    @Column
    private int totalPlaces;

    public Faculty(String nameRu, String nameEn, int budget, int total) {
        this.nameEn = nameEn;
        this.nameRu = nameRu;
        this.budgetPlaces = budget;
        this.totalPlaces = total;
    }

    public Faculty() {
    }
}
