package ru.test.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.test.core.model.dto.ImageResponse;
import ru.test.core.service.ImageService;

import java.time.OffsetDateTime;
import java.util.List;


@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<List<ImageResponse>> uploadImages(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("userId") Long userId) {
        List<ImageResponse> imageResponses = imageService.uploadImages(files, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(imageResponses);
    }

    @GetMapping("/")
    public ResponseEntity<List<ImageResponse>> getUserImages(
            @RequestParam("userId") Long userId,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortDirection", required = false) String sortDirection,
            @RequestParam(name = "minSize", required = false) Long minSize,
            @RequestParam(name = "maxSize", required = false) Long maxSize,
            @RequestParam(name = "startDate", required = false) OffsetDateTime startDate,
            @RequestParam(name = "endDate", required = false) OffsetDateTime endDate) {

        List<ImageResponse> images = imageService.getFilteredImages(userId, sortBy, sortDirection, minSize, maxSize, startDate, endDate);
        return ResponseEntity.ok(images);
    }


    @GetMapping("/{imageId}/download")
    public ResponseEntity<ImageResponse> downloadImage(
            @PathVariable Long imageId,
            @RequestParam("userId") Long userId) {
        ImageResponse imageResponse = imageService.downloadImage(imageId, userId);
        return ResponseEntity.ok(imageResponse);
    }

}