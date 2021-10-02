package com.example.unispring.repository;

import com.example.unispring.model.Grade;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GradeRepository extends CrudRepository<Grade, Long> {

    Grade findBySubjectIdAndApplicantIdAndExamType(
            Long subjectId, Long applicantId, String examType);

    List<Grade> findAllByApplicantId(Long id);

}
