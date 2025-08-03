package com.zwc.zwcbackend.repository;

import com.zwc.zwcbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
// This interface extends JpaRepository to provide CRUD operations for User entities.