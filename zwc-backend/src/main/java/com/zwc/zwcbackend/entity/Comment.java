package com.zwc.zwcbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Blog blog;

    // ✅ NEW: Parent comment for nesting
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // ✅ NEW: Depth level of comment (0 = root, 1 = reply, etc.)
    private int depth;

    // ✅ Optional: Store who liked this comment
    @ElementCollection
    private Set<Long> reactedUserIds = new HashSet<>();
}
