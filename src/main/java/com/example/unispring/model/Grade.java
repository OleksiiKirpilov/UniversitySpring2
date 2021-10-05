package com.example.unispring.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Grade entity.
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

    @Column
    private boolean confirmed;

    public Grade(Long subjectId, Long applicantId, int gradeValue, String examType) {
        this.subjectId = subjectId;
        this.applicantId = applicantId;
        this.grade = gradeValue;
        this.examType = examType;
        this.confirmed = false;
    }
}
