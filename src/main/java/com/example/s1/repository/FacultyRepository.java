package com.example.s1.repository;

import com.example.s1.model.Faculty;
import com.example.s1.model.Subject;
import org.springframework.data.repository.CrudRepository;

public interface FacultyRepository extends CrudRepository<Faculty, Long> {

    Faculty findByNameEn(String nameEn);

}
