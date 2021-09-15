package com.example.s1.repository;

import com.example.s1.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByLastName(String lastName);

    User findById(long id);

    User findByEmail(String email);

    User findUserByEmailAndPassword(String email, String password);

}
