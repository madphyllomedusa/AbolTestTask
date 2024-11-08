package ru.test.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.test.core.model.dto.ImageResponse;
import ru.test.core.service.ImageService;
import ru.test.core.service.UserService;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/moderator")
@RequiredArgsConstructor

public class ModeratorController {
    private final UserService userService;
    private final ImageService imageService;

    @PutMapping("/users/{userId}/block")
    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
        return ResponseEntity.ok("User has been blocked");
    }

    @PutMapping("/users/{userId}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
        userService.unblockUser(userId);
        return ResponseEntity.ok("User has been unblocked");
    }

    @GetMapping("/images")
    public ResponseEntity<List<ImageResponse>> getAllImages(
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortDirection", required = false) String sortDirection,
            @RequestParam(name = "minSize", required = false) Long minSize,
            @RequestParam(name = "maxSize", required = false) Long maxSize,
            @RequestParam(name = "startDate", required = false) OffsetDateTime startDate,
            @RequestParam(name = "endDate", required = false) OffsetDateTime endDate) {

        List<ImageResponse> images = imageService.getFilteredImages(
                userId,
                sortBy,
                sortDirection,
                minSize,
                maxSize,
                startDate,
                endDate);
        return ResponseEntity.ok(images);
    }
}
