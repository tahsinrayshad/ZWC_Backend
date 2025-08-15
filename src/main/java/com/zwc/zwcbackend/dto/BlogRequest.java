// src/main/java/com/zwc/zwcbackend/dto/BlogRequest.java
package com.zwc.zwcbackend.dto;

import com.zwc.zwcbackend.entity.User;
import lombok.Data;

@Data
public class BlogRequest {
    private String title;
    private String content;
}
// This class is used to encapsulate the request data for creating or updating a blog post.