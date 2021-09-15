package com.example.s1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Faculty subjects entity. Main purpose of this class is to tell which subjects
 * are needed to applicant, so then he can apply for some faculty. This subjects
 * are also called preliminary.
 */
@Entity
@Data
@NoArgsConstructor
public class FacultySubjects implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long subjectId;
    private Long facultyId;

    public FacultySubjects(Long subjectId, Long facultyId) {
        this.subjectId = subjectId;
        this.facultyId = facultyId;
    }
}
