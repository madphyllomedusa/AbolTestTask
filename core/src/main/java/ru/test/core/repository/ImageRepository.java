package ru.test.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.test.core.model.entity.Image;


public interface ImageRepository extends JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {

}

