package com.zwc.zwcbackend.service;

import com.zwc.zwcbackend.dto.BlogRequest;
import com.zwc.zwcbackend.dto.BlogResponse;
import com.zwc.zwcbackend.entity.Blog;
import com.zwc.zwcbackend.entity.Reaction;
import com.zwc.zwcbackend.entity.User;
import com.zwc.zwcbackend.repository.BlogRepository;
import com.zwc.zwcbackend.repository.ReactionRepository;
import com.zwc.zwcbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;


    // 🔐 Only logged-in users can create a blog
    public BlogResponse createBlog(BlogRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        Blog savedBlog = blogRepository.save(blog);
        return toResponse(savedBlog);
    }

    // 🔐 Only the owner can update their blog
    public BlogResponse updateBlog(Long blogId, BlogRequest request) {
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

        Blog updatedBlog = blogRepository.save(blog);
        return toResponse(updatedBlog);
    }

    // 🔐 Only owner or ADMIN can delete a blog
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
    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ✅ Public: Get blogs by user ID
    public List<BlogResponse> getBlogsByUserId(Long userId) {
        Long currentUserId = getCurrentUserId(); // 👈 fetch it here

        return blogRepository.findByUserId(userId)
                .stream()
                .map(blog -> toResponse(blog, currentUserId)) // 👈 pass both args manually
                .toList();
    }

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    public BlogResponse toResponse(Blog blog) {
        Long currentUserId = getCurrentUserId(); // You already wrote this method earlier
        return toResponse(blog, currentUserId);  // Delegate to the main two-arg method
    }

    // ✅ Internal mapper method
    public BlogResponse toResponse(Blog blog, Long currentUserId) {
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
        response.setReactCount(blog.getReactedUserIds().size());
        response.setLikedByCurrentUser(blog.getReactedUserIds().contains(currentUserId));

        return response;
    }

    // Get a single blog by ID
    public BlogResponse getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        return toResponse(blog);
    }


    public void toggleBlogReaction(Long blogId, String userEmail) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Reaction> existing = reactionRepository.findByUserAndBlog(user, blog);

        if (existing.isPresent()) {
            reactionRepository.delete(existing.get()); // Unlike
        } else {
            Reaction reaction = Reaction.builder()
                    .user(user)
                    .blog(blog)
                    .build();
            reactionRepository.save(reaction); // Like
        }
    }




}
