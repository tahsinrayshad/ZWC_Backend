package com.zwc.zwcbackend.controller;

import com.zwc.zwcbackend.dto.ItemRequest;
import com.zwc.zwcbackend.dto.ItemResponse;
import com.zwc.zwcbackend.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 🔐 Create a new item (authenticated users)
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(itemService.createItem(request));
    }

    // 🔐 Update an item (only owner)
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    // 🔐 Delete item (only owner)
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted successfully.");
    }

    // ✅ Public: Get all items
    @GetMapping("/all")
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    // ✅ Public: Get item by ID
    @GetMapping("/single/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    // 🔐 Get items by current authenticated user
    @GetMapping("/my-items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ItemResponse>> getMyItems() {
        return ResponseEntity.ok(itemService.getItemsByUser());
    }

    // Get items by item type
    @GetMapping("/type/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ItemResponse>> getItemsByType(@PathVariable String type) {
        return ResponseEntity.ok(itemService.getItemsByType(type));
    }

    // Get items by item condition
    @GetMapping("/condition/{condition}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ItemResponse>> getItemsByCondition(@PathVariable String condition) {
        return ResponseEntity.ok(itemService.getItemsByCondition(condition));
    }
}
