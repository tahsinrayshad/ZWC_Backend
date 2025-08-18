package com.zwc.zwcbackend.controller;

import com.zwc.zwcbackend.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/comment/{commentId}")
    public ResponseEntity<String> reactToComment(@PathVariable Long commentId, Principal principal) {
        reactionService.toggleCommentReaction(commentId, principal.getName());
        return ResponseEntity.ok("Toggled comment reaction");
    }

    @PostMapping("/blog/{blogId}")
    public ResponseEntity<String> reactToBlog(@PathVariable Long blogId, Principal principal) {
        reactionService.toggleBlogReaction(blogId, principal.getName());
        return ResponseEntity.ok("Toggled blog reaction");
    }

    @GetMapping("/comment/{commentId}/count")
    public ResponseEntity<Integer> getCommentReactionCount(@PathVariable Long commentId) {
        return ResponseEntity.ok(reactionService.getCommentReactionCount(commentId));
    }

    @GetMapping("/blog/{blogId}/count")
    public ResponseEntity<Integer> getBlogReactionCount(@PathVariable Long blogId) {
        return ResponseEntity.ok(reactionService.getBlogReactionCount(blogId));
    }
}

