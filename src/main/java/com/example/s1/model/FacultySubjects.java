package com.example.s1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column
    private Long subjectId;

    @Column
    private Long facultyId;

    public FacultySubjects(Long subjectId, Long facultyId) {
        this.subjectId = subjectId;
        this.facultyId = facultyId;
    }
}
