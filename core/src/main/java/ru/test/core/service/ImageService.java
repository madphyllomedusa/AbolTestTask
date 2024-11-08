package ru.test.core.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.test.core.model.dto.ImageResponse;
import ru.test.core.model.dto.UserDto;

import java.util.List;

public interface ImageService {
    List<ImageResponse> uploadImages(List<MultipartFile> files, Long userId);
    ImageResponse downloadImage(Long imageId, Long userId);
    List<ImageResponse> getUserImages(Long userId, String sortBy, String filterBy);
}
