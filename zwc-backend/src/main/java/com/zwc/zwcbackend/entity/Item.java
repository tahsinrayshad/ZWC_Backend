package com.zwc.zwcbackend.entity;

import com.zwc.zwcbackend.enums.ItemCondition;
import com.zwc.zwcbackend.enums.ItemStatus;
import com.zwc.zwcbackend.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many items can belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;           // e.g., Plastic, Electronics

    private String title;
    private String description;

    @Column(name = "item_condition") // Optional: specify DB column name
    @Enumerated(EnumType.STRING)
    private ItemCondition itemCondition;         // e.g., New, Used, Damaged

    private LocalDate expiryDate;
    private LocalDate manufacturingDate;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;              // e.g., Available, Donated, Discarded,
    private String location;            // e.g., "Uttara, Dhaka"
}
