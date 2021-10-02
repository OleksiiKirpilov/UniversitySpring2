package com.example.unispring.configuration;

import com.example.unispring.model.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/userRegistration").anonymous()
                .antMatchers("/applyFaculty").hasRole(Role.USER_ROLE_NAME)
                .antMatchers(
                        "/adminRegistration",
                        "/viewAllSubjects",
                        "/viewSubject",
                        "/addSubject",
                        "/editSubject",
                        "/deleteSubject",
                        "/addFaculty",
                        "/editFaculty",
                        "/deleteFaculty",
                        "/viewApplicant",
                        "/createReport",
                        "/confirmGrades"
                ).hasRole(Role.ADMIN_ROLE_NAME)
                .antMatchers("/viewProfile",
                        "/editProfile",
                        "/logout"
                ).hasAnyRole(Role.ADMIN_ROLE_NAME, Role.USER_ROLE_NAME)
                .antMatchers("/login",
                        "/viewAllFaculties",
                        "/viewFaculty",
                        "/setSessionLanguage").permitAll();
    }

}
