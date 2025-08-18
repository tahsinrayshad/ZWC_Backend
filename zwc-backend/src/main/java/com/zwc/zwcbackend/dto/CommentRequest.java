package com.zwc.zwcbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequest {
    private String content;
    private Long blogId;
    private Long parentId; // Optional for nested comments
}
