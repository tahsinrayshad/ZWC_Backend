package com.zwc.zwcbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private int depth;
    private Long parentId;
    private Long userId;
    private String userName;
    private List<CommentResponse> replies = new ArrayList<>(); // Nested replies
    private boolean reacted;
    private int likeCount;

}
