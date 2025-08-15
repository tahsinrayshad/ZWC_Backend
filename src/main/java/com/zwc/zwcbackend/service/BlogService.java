package com.zwc.zwcbackend.service;

import com.zwc.zwcbackend.dto.BlogRequest;
import com.zwc.zwcbackend.dto.BlogResponse;
import com.zwc.zwcbackend.entity.Blog;
import com.zwc.zwcbackend.entity.User;
import com.zwc.zwcbackend.repository.BlogRepository;
import com.zwc.zwcbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    // 🔐 Only logged-in users can create a blog
    public Blog createBlog(BlogRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        return blogRepository.save(blog);
    }

    // 🔐 Only owner can update their blog
    public Blog updateBlog(Long blogId, BlogRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        if (!blog.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to update this blog.");
        }

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());

        return blogRepository.save(blog);
    }

    // Delete Blog
    public void deleteBlog(Long id, String currentUserEmail) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        String blogOwnerEmail = blog.getUser().getEmail();
        boolean isAdmin = userRepository.findByEmail(currentUserEmail)
                .map(user -> user.getRole().name().equals("ADMIN"))
                .orElse(false);

        if (!blogOwnerEmail.equals(currentUserEmail) && !isAdmin) {
            throw new RuntimeException("You are not authorized to delete this blog.");
        }

        blogRepository.delete(blog);
    }


    // ✅ Public: Get all blogs
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    // ✅ Public: Get blogs by user ID
    public List<Blog> getBlogsByUserId(Long userId) {
        return blogRepository.findByUserId(userId);
    }

    public BlogResponse toResponse(Blog blog) {
        BlogResponse response = new BlogResponse();
        response.setId(blog.getId());
        response.setTitle(blog.getTitle());
        response.setContent(blog.getContent());

        BlogResponse.BlogUser userDto = new BlogResponse.BlogUser();
        userDto.setId(blog.getUser().getId());
        userDto.setName(blog.getUser().getName());
        userDto.setEmail(blog.getUser().getEmail());
        userDto.setAddress(blog.getUser().getAddress());
        userDto.setContactNumber(blog.getUser().getContactNumber());
        userDto.setRole(blog.getUser().getRole().name());

        response.setUser(userDto);
        return response;
    }

}
