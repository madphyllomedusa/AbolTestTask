package ru.test.core.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.test.core.exceptionhandler.BadRequestException;
import ru.test.core.exceptionhandler.NotFoundException;
import ru.test.core.model.dto.ImageResponse;
import ru.test.core.model.entity.Image;
import ru.test.core.model.entity.User;
import ru.test.core.model.mappers.ImageMapper;
import ru.test.core.repository.ImageRepository;
import ru.test.core.repository.UserRepository;
import ru.test.core.service.CloudinaryService;
import ru.test.core.service.ImageService;
import ru.test.core.specification.ImageSpecification;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ImageMapper imageMapper;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    ;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final List<String> ALLOWED_FILE_TYPES = List.of("image/jpeg", "image/png");

    @Override
    @Transactional
    public List<ImageResponse> uploadImages(List<MultipartFile> files, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));


        List<CompletableFuture<ImageResponse>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() ->
                        uploadSingleImage(file, user), executorService))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }


    @Override
    public ImageResponse downloadImage(Long imageId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Изображение не найдено"));

        if (!image.getUser().equals(user)) {
            logger.error("Access denied: Image does not belong to user with ID {}", userId);
            throw new SecurityException("Доступ запрещен: изображение принадлежит другому пользователю.");
        }

        return imageMapper.toDto(image);
    }

    @Override
    public List<ImageResponse> getFilteredImages(Long userId,
                                                 String sortBy,
                                                 String sortDirection,
                                                 Long minSize, Long maxSize,
                                                 OffsetDateTime startDate,
                                                 OffsetDateTime endDate) {
        logger.info("Fetching images with filters - User ID: {}," +
                        " Sort By: {}," +
                        " Sort Direction: {}," +
                        " Min Size: {}," +
                        " Max Size: {}," +
                        " Start Date: {}," +
                        " End Date: {}",
                userId, sortBy, sortDirection, minSize, maxSize, startDate, endDate);

        Specification<Image> spec = Specification.where(userId != null ? ImageSpecification.hasUserId(userId) : null)
                .and(ImageSpecification.hasMinSize(minSize))
                .and(ImageSpecification.hasMaxSize(maxSize))
                .and(ImageSpecification.hasStartDate(startDate))
                .and(ImageSpecification.hasEndDate(endDate));

        Sort sort = Sort.by(
                Sort.Direction.fromString(sortDirection != null ? sortDirection : "ASC"),
                sortBy != null ? sortBy : "id");
        List<Image> images = imageRepository.findAll(spec, sort);

        return images.stream()
                .map(imageMapper::toDto)
                .collect(Collectors.toList());
    }

    private ImageResponse uploadSingleImage(MultipartFile file, User user) {
        validateFile(file);

        logger.info("Uploading image for user with ID: {}, file: {}", user.getId(), file.getOriginalFilename());
        String url = cloudinaryService.upload(file);

        Image image = imageMapper.createImage(file, user, url);

        imageRepository.save(image);
        logger.info("Image uploaded successfully with URL: {}", url);

        return imageMapper.toDto(image);
    }

    private void validateFile(MultipartFile file) {
        if(file.isEmpty()){
            logger.error("File is empty {}", file.getOriginalFilename());
            throw new BadRequestException("Передан пустой файл" + file.getOriginalFilename());
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            logger.error("File size exceeds the maximum limit for file: {}", file.getOriginalFilename());
            throw new BadRequestException("Файл слишком большой: " + file.getOriginalFilename());
        }

        if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            logger.error("Unsupported file type for file: {}", file.getOriginalFilename());
            throw new BadRequestException("Неподдерживаемый формат файла: " + file.getOriginalFilename() +
                    ".  Разрешенные форматы: JPEG и PNG.");
        }
    }

}
