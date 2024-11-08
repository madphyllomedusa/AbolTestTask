package ru.test.core.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.test.core.model.entity.Image;

import java.time.OffsetDateTime;

public class ImageSpecification {

    public static Specification<Image> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Image> hasMinSize(Long minSize) {
        return (root, query, criteriaBuilder) ->
                minSize == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("size"), minSize);
    }

    public static Specification<Image> hasMaxSize(Long maxSize) {
        return (root, query, criteriaBuilder) ->
                maxSize == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("size"), maxSize);
    }

    public static Specification<Image> hasStartDate(OffsetDateTime startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("uploadDate"), startDate);
    }

    public static Specification<Image> hasEndDate(OffsetDateTime endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("uploadDate"), endDate);
    }
}
