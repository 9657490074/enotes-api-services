package com.org.enotesapiservice.repository;

import com.org.enotesapiservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByEmail(String email);

    User findByEmail(String email);
}
