package com.example.unispring.repository;

import com.example.unispring.model.FacultySubjects;
import org.springframework.data.repository.CrudRepository;

public interface FacultySubjectsRepository extends CrudRepository<FacultySubjects, Long> {

    void deleteAllByFacultyId(Long id);

    void deleteByFacultyIdAndSubjectId(Long facultyId, Long subjectId);

    boolean existsAllBySubjectId(Long id);

}
