package com.example.s1.repository;

import com.example.s1.model.Grade;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GradeRepository extends CrudRepository<Grade, Long> {

    Grade findBySubjectIdAndApplicantIdAndExamType(
            Long subjectId, Long applicantId, String examType);

    List<Grade> findAllByApplicantId(Long id);

}
