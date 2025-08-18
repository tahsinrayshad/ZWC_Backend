package com.zwc.zwcbackend.repository;

import com.zwc.zwcbackend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByUserId(Long userId);
}
// This interface extends JpaRepository to provide CRUD operations for Blog entities.