package com.example.s1.repository;

import com.example.s1.model.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubjectRepository extends CrudRepository<Subject, Long> {

    @Query(value = "SELECT subjects.id, subjects.name_ru, subjects.name_en FROM subjects, faculty_subjects " +
                   "WHERE faculty_subjects.faculty_id = ? AND faculty_subjects.subject_id = subjects.id",
            nativeQuery = true)
    Iterable<Subject> findAllByFacultyId(Long id);

    @Query(value = "SELECT subjects.id, subjects.name_ru, subjects.name_en FROM subjects, faculty_subjects " +
            "WHERE faculty_subjects.faculty_id <> ? AND faculty_subjects.subject_id = subjects.id",
            nativeQuery = true)
    Iterable<Subject> findAllByFacultyIdNotEquals(Long id);

    Subject findSubjectByNameEnEquals(String nameEn);
}
