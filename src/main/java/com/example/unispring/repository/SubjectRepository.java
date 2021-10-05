package com.example.unispring.repository;

import com.example.unispring.model.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubjectRepository extends CrudRepository<Subject, Long> {

    @Query(value = "SELECT subjects.id, subjects.name_ru, subjects.name_en FROM subjects, faculty_subjects " +
                   "WHERE faculty_subjects.faculty_id = ? AND faculty_subjects.subject_id = subjects.id",
            nativeQuery = true)
    List<Subject> findAllByFacultyId(Long id);

    @Query(value = "SELECT subjects.id, subjects.name_ru, subjects.name_en FROM subjects " +
            "LEFT JOIN faculty_subjects ON faculty_subjects.subject_id = subjects.id " +
            " AND faculty_subjects.faculty_id = ? WHERE faculty_subjects.id IS NULL",
            nativeQuery = true)
    List<Subject> findAllByFacultyIdNotEquals(Long id);

    Subject findSubjectByNameEnEquals(String nameEn);

    List<Subject> findAll();
}
