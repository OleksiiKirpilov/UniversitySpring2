package com.example.unispring.repository;

import com.example.unispring.model.FacultyApplicants;
import org.springframework.data.repository.CrudRepository;

public interface FacultyApplicantsRepository extends CrudRepository<FacultyApplicants, Long> {

    FacultyApplicants findByFacultyIdAndApplicantId(Long facultyId, Long applicantId);

}
