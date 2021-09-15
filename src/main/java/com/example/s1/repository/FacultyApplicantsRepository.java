package com.example.s1.repository;

import com.example.s1.model.FacultyApplicants;
import org.springframework.data.repository.CrudRepository;

public interface FacultyApplicantsRepository extends CrudRepository<FacultyApplicants, Long> {

    FacultyApplicants findByFacultyIdAndApplicantId(Long facultyId, Long applicantId);

}
