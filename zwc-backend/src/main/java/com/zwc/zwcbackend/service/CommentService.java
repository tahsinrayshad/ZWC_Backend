package com.zwc.zwcbackend.service;

import com.zwc.zwcbackend.dto.CommentRequest;
import com.zwc.zwcbackend.dto.CommentResponse;
import com.zwc.zwcbackend.entity.Blog;
import com.zwc.zwcbackend.entity.Comment;
import com.zwc.zwcbackend.entity.Reaction;
import com.zwc.zwcbackend.entity.User;
import com.zwc.zwcbackend.repository.BlogRepository;
import com.zwc.zwcbackend.repository.CommentRepository;
import com.zwc.zwcbackend.repository.ReactionRepository;
import com.zwc.zwcbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    // ✅ Create comment (or nested reply)
    public CommentResponse createComment(CommentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Blog blog = blogRepository.findById(request.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        Comment parent = null;
        int depth = 0;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            depth = parent.getDepth() + 1;
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .blog(blog)
                .user(user)
                .parent(parent)
                .depth(depth)
                .build();

        return toResponse(commentRepository.save(comment));
    }

    // ✅ Update comment (by owner only)
    public CommentResponse updateComment(Long id, CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!comment.getUser().getEmail().equals(currentUser)) {
            throw new RuntimeException("Unauthorized");
        }

        comment.setContent(request.getContent());
        return toResponse(commentRepository.save(comment));
    }

    // ✅ Delete comment (by owner only)
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!comment.getUser().getEmail().equals(currentUser)) {
            throw new RuntimeException("Unauthorized");
        }

        commentRepository.delete(comment);
    }

    // ✅ Get all comments of a blog (with nested replies)
    public List<CommentResponse> getCommentsByBlogId(Long blogId) {
        List<Comment> comments = commentRepository.findByBlogId(blogId);

        Map<Long, CommentResponse> commentMap = new HashMap<>();
        List<CommentResponse> roots = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponse dto = toResponse(comment);
            commentMap.put(comment.getId(), dto);
        }

        for (Comment comment : comments) {
            CommentResponse dto = commentMap.get(comment.getId());
            if (comment.getParent() != null) {
                CommentResponse parentDto = commentMap.get(comment.getParent().getId());
                parentDto.getReplies().add(dto);
            } else {
                roots.add(dto);
            }
        }

        return roots;
    }

    // ✅ React (like/unlike) to a comment
    public void toggleCommentReaction(Long commentId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Optional<Reaction> existing = reactionRepository.findByUserAndComment(user, comment);
        if (existing.isPresent()) {
            reactionRepository.delete(existing.get()); // Unlike
        } else {
            Reaction reaction = Reaction.builder()
                    .user(user)
                    .comment(comment)
                    .build();
            reactionRepository.save(reaction); // Like
        }
    }

    // 🔁 Helper to convert entity → response
    private CommentResponse toResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setDepth(comment.getDepth());
        response.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        response.setUserId(comment.getUser().getId());
        response.setUserName(comment.getUser().getName());

        int likeCount = reactionRepository.countByComment(comment);
        response.setLikeCount(likeCount);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        boolean reacted = userOpt
                .flatMap(user -> reactionRepository.findByUserAndComment(user, comment))
                .isPresent();
        response.setReacted(reacted);

        return response;
    }
}
