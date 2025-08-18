package com.zwc.zwcbackend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogResponseDTO {
    private Long id;
    private String title;
    private String content;
    private UserResponseDTO user;

}
