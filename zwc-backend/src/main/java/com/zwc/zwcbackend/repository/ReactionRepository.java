package com.zwc.zwcbackend.repository;

import com.zwc.zwcbackend.entity.Blog;
import com.zwc.zwcbackend.entity.Comment;
import com.zwc.zwcbackend.entity.Reaction;
import com.zwc.zwcbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserAndComment(User user, Comment comment);
    Optional<Reaction> findByUserAndBlog(User user, Blog blog);
    int countByComment(Comment comment);
    int countByBlog(Blog blog);
    void deleteByUserAndComment(User user, Comment comment);
    void deleteByUserAndBlog(User user, Blog blog);

}
