package com.example.s1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Grade entity. Every instance is characterized by foreign keys from subject and
 * applicant, grade value. So every grade references to specific subject and
 * applicant that get it on some exam.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "grades")
public class Grade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long subjectId;

    @Column
    private Long applicantId;

    @Column
    private int grade;

    @Column
    private String examType;

    public Grade(Long subjectId, Long applicantId, int gradeValue, String examType) {
        this.subjectId = subjectId;
        this.applicantId = applicantId;
        this.grade = gradeValue;
        this.examType = examType;
    }
}
