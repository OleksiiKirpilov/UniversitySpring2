package com.example.s1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Grade entity. Every instance is characterized by foreign keys from subject and
 * applicant, grade value. So every grade references to specific subject and
 * applicant that get it on some exam.
 */
@Entity
@Data
@Table(name = "grades")
public class Grade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long subjectId;
    private Long applicantId;
    private int grade;
    private String examType;

    public Grade(Long subjectId, Long id, int gradeValue, String examType) {
    }
}
