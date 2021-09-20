package com.example.s1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportSheetView implements Serializable {

    private Long facultyId;

    private String firstName;

    private String lastName;

    private String email;

    private boolean blocked;

    private int preliminarySum;

    private int diplomaSum;

    private int totalSum;

    private boolean entered;

    private boolean enteredOnBudget;

}