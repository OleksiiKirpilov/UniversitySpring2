package com.example.s1.repository;

import com.example.s1.model.ReportSheet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportSheetRepository extends CrudRepository<ReportSheet, Long> {

    List<ReportSheet> findAllByFacultyId(Long id);
}
