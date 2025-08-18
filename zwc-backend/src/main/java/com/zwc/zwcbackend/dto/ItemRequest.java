package com.zwc.zwcbackend.dto;

import com.zwc.zwcbackend.enums.ItemCondition;
import com.zwc.zwcbackend.enums.ItemType;
import com.zwc.zwcbackend.enums.ItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ItemRequest {
    private ItemType itemType;
    private String title;
    private String description;
    private ItemCondition condition;
    private LocalDate expiryDate;
    private LocalDate manufacturingDate;

    // Optional — will default if not provided
    private ItemStatus status;
    private String location;
}
