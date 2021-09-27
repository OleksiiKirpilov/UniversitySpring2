package com.example.s1.repository;

import com.example.s1.model.Faculty;
import org.springframework.data.repository.CrudRepository;

public interface FacultyRepository extends CrudRepository<Faculty, Long> {

    Faculty findByNameEn(String nameEn);

    Faculty findByNameRu(String nameRu);

}
