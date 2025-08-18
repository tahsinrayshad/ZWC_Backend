package com.zwc.zwcbackend.service;

import com.zwc.zwcbackend.dto.ItemRequest;
import com.zwc.zwcbackend.dto.ItemResponse;
import com.zwc.zwcbackend.entity.Item;
import com.zwc.zwcbackend.entity.User;
import com.zwc.zwcbackend.enums.ItemCondition;
import com.zwc.zwcbackend.enums.ItemStatus;
import com.zwc.zwcbackend.enums.ItemType;
import com.zwc.zwcbackend.repository.ItemRepository;
import com.zwc.zwcbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final AuthService authService;


    // ✅ Create item
    public ItemResponse createItem(ItemRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Apply default status and location if not provided
        ItemStatus status = (request.getStatus() != null) ? request.getStatus() : ItemStatus.AVAILABLE;
        String location = (request.getLocation() != null) ? request.getLocation() : user.getAddress();

        Item item = Item.builder()
                .user(user)
                .itemType(request.getItemType())
                .title(request.getTitle())
                .description(request.getDescription())
                .itemCondition(request.getCondition())
                .expiryDate(request.getExpiryDate())
                .manufacturingDate(request.getManufacturingDate())
                .status(status)
                .location(location)
                .build();

        return toResponse(itemRepository.save(item));
    }

    // ✅ Update item
    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!item.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to update this item");
        }

        item.setItemType(request.getItemType());
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setItemCondition(request.getCondition());
        item.setExpiryDate(request.getExpiryDate());
        item.setManufacturingDate(request.getManufacturingDate());
        item.setStatus(ItemStatus.AVAILABLE); // Default status if not provided
        item.setLocation(authService.getCurrentUser().getAddress());

        return toResponse(itemRepository.save(item));
    }

    // ✅ Delete item
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!item.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this item");
        }

        itemRepository.delete(item);
    }

    // ✅ Get all items
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get items by user
    public List<ItemResponse> getItemsByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return itemRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get items by type
    public List<ItemResponse> getItemsByType(String type) {
        return itemRepository.findByItemType(ItemType.valueOf(type.toUpperCase()))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get items by condition
    public List<ItemResponse> getItemsByCondition(String condition) {
        return itemRepository.findByItemCondition(ItemCondition.valueOf(condition.toUpperCase()))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return toResponse(item);
    }

    // 🔁 Helper: entity → DTO
    private ItemResponse toResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setUserId(item.getUser().getId());
        response.setUserName(item.getUser().getName());
        response.setItemType(item.getItemType());
        response.setTitle(item.getTitle());
        response.setDescription(item.getDescription());
        response.setCondition(item.getItemCondition());
        response.setExpiryDate(item.getExpiryDate());
        response.setManufacturingDate(item.getManufacturingDate());
        response.setStatus(item.getStatus());
        response.setLocation(item.getLocation());
        return response;
    }
}
