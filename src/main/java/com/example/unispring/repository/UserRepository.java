package com.example.unispring.repository;

import com.example.unispring.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

    User findUserByEmailAndPassword(String email, String password);

}
