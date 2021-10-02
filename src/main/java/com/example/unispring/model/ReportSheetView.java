package com.example.unispring.model;

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

    public ReportSheetView(ReportSheet rs) {
        facultyId = rs.getFacultyId();
        firstName = rs.getFirstName();
        lastName = rs.getLastName();
        email = rs.getEmail();
        blocked = rs.isBlocked();
        preliminarySum = rs.getPreliminarySum();
        diplomaSum = rs.getDiplomaSum();
        totalSum = rs.getTotalSum();
        entered = rs.isEntered();
        enteredOnBudget = rs.isEnteredOnBudget();
    }

}
