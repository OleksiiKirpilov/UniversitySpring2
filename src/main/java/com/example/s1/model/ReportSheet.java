package com.example.s1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table
@NoArgsConstructor
public class ReportSheet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long facultyId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private boolean blocked;

    @Column
    private int preliminarySum;

    @Column
    private int diplomaSum;

    @Column
    private int totalSum;

    @Column
    private boolean entered;

    @Column
    private boolean enteredOnBudget;
}
