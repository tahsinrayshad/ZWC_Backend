package com.zwc.zwcbackend.entity;

import com.zwc.zwcbackend.entity.Blog;
import com.zwc.zwcbackend.entity.Comment;
import com.zwc.zwcbackend.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Only one of these will be non-null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
