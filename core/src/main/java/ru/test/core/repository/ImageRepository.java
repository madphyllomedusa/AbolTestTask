package ru.test.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.test.core.model.entity.Image;
import ru.test.core.model.entity.User;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByUser(User user);
    List<Image> findByUserOrderBySize(User user);
    List<Image> findByUserOrderByUploadDate(User user);
    Image findByIdAndUser(Long imageId, User user);
}

