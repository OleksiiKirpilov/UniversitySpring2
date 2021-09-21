package com.example.s1.repository;

import com.example.s1.model.Grade;
import org.springframework.data.repository.CrudRepository;

public interface GradeRepository extends CrudRepository<Grade, Long> {

    public Grade findBySubjectIdAndApplicantIdAndExamType(
            Long subjectId, Long applicantId, String examType);
}
