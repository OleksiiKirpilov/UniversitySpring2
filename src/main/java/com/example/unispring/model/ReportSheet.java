package com.example.unispring.model;

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

    public ReportSheet(ReportSheetView rs) {
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
