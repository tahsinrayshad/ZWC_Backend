package com.zwc.zwcbackend.repository;

import com.zwc.zwcbackend.entity.Item;
import com.zwc.zwcbackend.entity.User;
import com.zwc.zwcbackend.enums.ItemCondition;
import com.zwc.zwcbackend.enums.ItemStatus;
import com.zwc.zwcbackend.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Get items by a specific user
    List<Item> findByUserId(Long userId);

    // Optional: Filter by status (if needed)
    List<Item> findByStatus(ItemStatus status);

    // Optional: Filter by type or condition
    List<Item> findByItemType(ItemType type);
    List<Item> findByItemCondition(ItemCondition itemCondition);

    List<Item> findByUser(User user);

}
