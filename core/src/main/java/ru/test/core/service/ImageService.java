package ru.test.core.service;

import org.springframework.web.multipart.MultipartFile;
import ru.test.core.model.dto.ImageResponse;

import java.time.OffsetDateTime;
import java.util.List;

public interface ImageService {
    List<ImageResponse> uploadImages(List<MultipartFile> files, Long userId);
    ImageResponse downloadImage(Long imageId, Long userId);
    List<ImageResponse> getFilteredImages(Long userId,
                                      String sortBy,
                                      String sortDirection,
                                      Long minSize,
                                      Long maxSize,
                                      OffsetDateTime startDate,
                                      OffsetDateTime endDate);

}
