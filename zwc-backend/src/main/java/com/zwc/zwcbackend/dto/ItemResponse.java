package com.zwc.zwcbackend.dto;

import com.zwc.zwcbackend.enums.ItemCondition;
import com.zwc.zwcbackend.enums.ItemStatus;
import com.zwc.zwcbackend.enums.ItemType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ItemResponse {
    private Long id;
    private Long userId;
    private String userName;

    private ItemType itemType;
    private String title;
    private String description;
    private ItemCondition condition;
    private LocalDate expiryDate;
    private LocalDate manufacturingDate;
    private ItemStatus status;
    private String location;
}
