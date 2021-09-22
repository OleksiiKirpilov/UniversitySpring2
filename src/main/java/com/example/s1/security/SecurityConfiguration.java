package com.example.s1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
                .antMatchers("/user/user_view_faculty").permitAll()
                .antMatchers("/user/user_add_user").permitAll()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/user/**").hasRole("user")
                .antMatchers(
                        "/adminRegistration",
                        "/viewAllSubjects",
                        "/editFaculty",
                        "/addFaculty",
                        "/editSubject",
                        "/viewApplicant"
                            ).hasRole("admin")
                .antMatchers("/editProfile").hasAnyRole("admin", "user")
                .antMatchers("/").permitAll()
//                .and().formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .permitAll()
//                .defaultSuccessUrl("/login")
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