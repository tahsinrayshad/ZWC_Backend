package com.zwc.zwcbackend.controller;

import com.zwc.zwcbackend.dto.CommentRequest;
import com.zwc.zwcbackend.dto.CommentResponse;
import com.zwc.zwcbackend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted");
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long blogId) {
        return ResponseEntity.ok(commentService.getCommentsByBlogId(blogId));
    }

    @PostMapping("/{id}/react")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> toggleReact(@PathVariable Long id) {
        commentService.toggleCommentReaction(id);
        return ResponseEntity.ok("Reaction toggled successfully");
    }
}
