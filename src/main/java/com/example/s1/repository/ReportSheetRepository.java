package com.example.s1.repository;

import com.example.s1.model.ReportSheet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportSheetRepository extends CrudRepository<ReportSheet, Long> {

    @Query(nativeQuery = true, value =
            "SELECT row_number() OVER (ORDER BY first_name, last_name) id, " +
                    "faculty_id, first_name, last_name, users.email, " +
                    "applicants.blocked AS blocked, " +
                    "preliminary_sum, diploma_sum, preliminary_sum + diploma_sum AS total_sum, " +
                    "false AS entered, false AS entered_on_budget " +
                    "FROM applicants_grades_sum " +
                    "INNER JOIN faculties ON applicants_grades_sum.faculty_id = faculties.id " +
                    "INNER JOIN applicants ON applicant_id = applicants.id " +
                    "INNER JOIN users ON applicants.users_id = users.id " +
                    "WHERE faculty_id = ? " +
                    "ORDER BY blocked ASC , `total_sum` DESC")
    List<ReportSheet> computeAllByFacultyId(Long id);

}
