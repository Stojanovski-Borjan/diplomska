package com.example.bookthebook.repo;

import com.example.bookthebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findUserByEmailAndPassword(String email, String password);

    boolean existsByEmailAndPassword(String email, String password);
}
