package com.example.unispring.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Faculty applicants entity. This is a compound entity class, that tells which
 * applicant applied for which faculty by referencing to their foreign keys.
 */
@Entity
@Data
@NoArgsConstructor
public class FacultyApplicants implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long facultyId;

    @Column
    private Long applicantId;


}
