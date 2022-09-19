package com.technews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.technews.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email) throws Exception;
}
