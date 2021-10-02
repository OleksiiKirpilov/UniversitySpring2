package com.example.unispring.repository;

import com.example.unispring.model.Faculty;
import org.springframework.data.repository.CrudRepository;

public interface FacultyRepository extends CrudRepository<Faculty, Long> {

    Faculty findByNameEn(String nameEn);

    Faculty findByNameRu(String nameRu);

}
