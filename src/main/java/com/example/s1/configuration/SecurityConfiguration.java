package com.example.s1.configuration;

import com.example.s1.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String SQL_FIND_USER_BY_NAME =
            "select email, password, 'true' from users where email = ?";
    private static final String SQL_FIND_AUTHORITIES_BY_NAME =
            "select email, role from users where email = ?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(SQL_FIND_USER_BY_NAME)
                .authoritiesByUsernameQuery(SQL_FIND_AUTHORITIES_BY_NAME)
                .passwordEncoder(getPasswordEncoder());
    }

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
                        "/setSessionLanguage").permitAll()
        ;
    }

    @Bean
    public static NoOpPasswordEncoder getPasswordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
//
//    @Bean
//    public PasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
