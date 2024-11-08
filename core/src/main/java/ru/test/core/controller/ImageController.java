package ru.test.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.test.core.model.dto.ImageResponse;
import ru.test.core.service.ImageService;

import java.util.List;


@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<List<ImageResponse>> uploadImages(@RequestPart("files") List<MultipartFile> files,
                                                            @RequestParam("userId") Long userId) {
        List<ImageResponse> imageResponses = imageService.uploadImages(files, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(imageResponses);
    }

    @GetMapping("/")
    public ResponseEntity<List<ImageResponse>> getUserImages(@RequestParam("userId") Long userId,
                                                             @RequestParam(name = "sortBy", required = false) String sortBy,
                                                             @RequestParam(name = "filterBy", required = false) String filterBy) {
        List<ImageResponse> images = imageService.getUserImages(userId, sortBy, filterBy);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{imageId}/download")
    public ResponseEntity<ImageResponse> downloadImage(@PathVariable Long imageId,
                                                       @RequestParam("userId") Long userId) {
        ImageResponse imageResponse = imageService.downloadImage(imageId, userId);
        return ResponseEntity.ok(imageResponse);
    }

}