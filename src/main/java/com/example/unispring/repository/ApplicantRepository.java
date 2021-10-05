package com.example.unispring.repository;

import com.example.unispring.model.Applicant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicantRepository extends CrudRepository<Applicant, Long> {

    Applicant findByUserId(Long userId);

    @Query(value = "SELECT applicants.* FROM applicants INNER JOIN faculty_applicants" +
            " ON faculty_applicants.applicant_id = applicants.id  WHERE faculty_applicants.faculty_id = ?",
            nativeQuery = true)
    List<Applicant> findAllByFacultyId(Long id);


}
