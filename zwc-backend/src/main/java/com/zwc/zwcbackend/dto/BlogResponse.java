package com.zwc.zwcbackend.dto;

import lombok.Data;

@Data
public class BlogResponse {
    private Long id;
    private String title;
    private String content;

    private BlogUser user;

    private int reactCount;             // total number of likes
    private boolean likedByCurrentUser; // true if the current user liked this blog

    @Data
    public static class BlogUser {
        private Long id;
        private String name;
        private String email;
        private String address;
        private String contactNumber;
        private String role;
    }
}
