package com.example.s1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Applicant entity. Has city, district, school, foreign user id field
 * and blocked state, which may be changed by admin.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "applicants")  //(name ="applicants")
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
    private boolean blocked;

    public Applicant(String city, String district, String school, Long userId, boolean blocked) {
        this.city = city;
        this.district = district;
        this.school = school;
        this.userId = userId;
        this.blocked = blocked;
    }
}
