package com.example.s1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Applicant entity. This transfer object characterized by city, district,
 * school, foreign user id field and blocked state, which is false by default,
 * but may be changed by admin.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name ="applicants")
public class Applicant implements Serializable {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String school;

    @Column(name = "users_id")
    private Long userId;

    @Column(nullable = false)
    private boolean isBlocked;

    public Applicant(String city, String district, String school, Long userId, boolean isBlocked) {
        this.city = city;
        this.district = district;
        this.school = school;
        this.userId = userId;
        this.isBlocked = isBlocked;
    }
}
