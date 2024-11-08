package ru.test.core.service.Impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import ru.test.core.exceptionhandler.BadRequestException;
import ru.test.core.model.dto.ImageResponse;
import ru.test.core.model.entity.Image;
import ru.test.core.model.entity.User;
import ru.test.core.model.mappers.ImageMapper;
import ru.test.core.model.mappers.UserMapper;
import ru.test.core.repository.ImageRepository;
import ru.test.core.repository.UserRepository;
import ru.test.core.service.CloudinaryService;
import ru.test.core.service.ImageService;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ImageMapper imageMapper;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final List<String> ALLOWED_FILE_TYPES = List.of("image/jpeg", "image/png");

    @Override
    @Transactional
    public List<ImageResponse> uploadImages(List<MultipartFile> files, Long userId) {
        List<ImageResponse> responses = new ArrayList<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    String errorMessage = "User not found with ID: " + userId;
                    logger.error(errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        for (MultipartFile file : files) {
            // Проверка размера файла
            if (file.getSize() > MAX_FILE_SIZE) {
                logger.error("File size exceeds the maximum limit for file: {}", file.getOriginalFilename());
                throw new IllegalArgumentException("File size exceeds the maximum limit of 10MB for file: " + file.getOriginalFilename());
            }

            // Проверка типа файла
            if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
                logger.error("Unsupported file type for file: {}", file.getOriginalFilename());
                throw new IllegalArgumentException("Unsupported file type for file: " + file.getOriginalFilename() + ". Only JPEG and PNG images are allowed.");
            }

            logger.info("Uploading image for user with ID: {}, file: {}", userId, file.getOriginalFilename());
            String url = cloudinaryService.upload(file);

            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setSize(file.getSize());
            image.setUploadDate(OffsetDateTime.now());
            image.setUser(user);
            image.setUrl(url);

            imageRepository.save(image);
            logger.info("Image uploaded successfully with URL: {}", url);

            responses.add(imageMapper.toDto(image));
        }

        return responses;
    }


    @Override
    public ImageResponse downloadImage(Long imageId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));

        if (!image.getUser().equals(user)) {
            logger.error("Access denied: Image does not belong to user with ID {}", userId);
            throw new SecurityException("Access denied: Image does not belong to the user.");
        }

        return imageMapper.toDto(image);
    }


    @Override
    public List<ImageResponse> getUserImages(Long userId, String sortBy, String filterBy) {
        logger.info("Fetching images for user ID: {}, sortBy: {}, filterBy: {}", userId, sortBy, filterBy);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            throw new IllegalArgumentException("User not found");
        }

        List<Image> images;
        if (filterBy == null || filterBy.isEmpty()) {
            images = imageRepository.findByUser(user);
        } else {
            switch (filterBy) {
                case "size":
                    images = imageRepository.findByUserOrderBySize(user);
                    break;
                case "uploadDate":
                    images = imageRepository.findByUserOrderByUploadDate(user);
                    break;
                default:
                    images = imageRepository.findByUser(user);
                    break;
            }
        }

        return images.stream()
                .map(imageMapper::toDto)
                .collect(Collectors.toList());
    }


}
