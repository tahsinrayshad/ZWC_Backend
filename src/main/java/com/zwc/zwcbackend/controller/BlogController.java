package com.zwc.zwcbackend.controller;

import com.zwc.zwcbackend.dto.BlogRequest;
import com.zwc.zwcbackend.dto.BlogResponse;
import com.zwc.zwcbackend.entity.Blog;
import com.zwc.zwcbackend.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // 🔐 Create blog (only authenticated users)
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody BlogRequest request) {
        Blog blog = blogService.createBlog(request);
        return ResponseEntity.ok(blogService.toResponse(blog));
    }

    // 🔐 Update blog (only authenticated & owner)
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BlogResponse> updateBlog(@PathVariable Long id, @Valid @RequestBody BlogRequest request) {
        Blog blog = blogService.updateBlog(id, request);
        return ResponseEntity.ok(blogService.toResponse(blog));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteBlog(@PathVariable Long id, Principal principal) {
        blogService.deleteBlog(id, principal.getName());
        return ResponseEntity.ok("Blog deleted successfully");
    }


    // ✅ Public: Fetch all blogs
    @GetMapping("/all")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    // ✅ Public: Fetch blogs by specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Blog>> getBlogsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(blogService.getBlogsByUserId(userId));
    }
}
