package com.zwc.zwcbackend.service;

import com.zwc.zwcbackend.entity.Comment;
import com.zwc.zwcbackend.entity.Reaction;
import com.zwc.zwcbackend.entity.User;
import com.zwc.zwcbackend.entity.Blog;
import com.zwc.zwcbackend.repository.BlogRepository;
import com.zwc.zwcbackend.repository.CommentRepository;
import com.zwc.zwcbackend.repository.ReactionRepository;
import com.zwc.zwcbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public void toggleCommentReaction(Long commentId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        Optional<Reaction> existing = reactionRepository.findByUserAndComment(user, comment);

        if (existing.isPresent()) {
            reactionRepository.delete(existing.get());
        } else {
            Reaction reaction = Reaction.builder().comment(comment).user(user).build();
            reactionRepository.save(reaction);
        }
    }

    public void toggleBlogReaction(Long blogId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Blog blog = blogRepository.findById(blogId).orElseThrow();

        Optional<Reaction> existing = reactionRepository.findByUserAndBlog(user, blog);

        if (existing.isPresent()) {
            reactionRepository.delete(existing.get());
        } else {
            Reaction reaction = Reaction.builder().blog(blog).user(user).build();
            reactionRepository.save(reaction);
        }
    }

    public int getCommentReactionCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return reactionRepository.countByComment(comment);
    }

    public int getBlogReactionCount(Long blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow();
        return reactionRepository.countByBlog(blog);
    }
}

