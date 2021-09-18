package com.example.s1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class ReportSheet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long facultyId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isBlocked;
    private int preliminarySum;
    private int diplomaSum;
    private int totalSum;

    private boolean entered;
    private boolean enteredOnBudget;
}
