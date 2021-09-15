package com.example.s1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private Long facultyId;
    private Long applicantId;

}
