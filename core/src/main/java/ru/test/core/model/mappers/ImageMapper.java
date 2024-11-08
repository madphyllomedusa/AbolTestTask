package ru.test.core.model.mappers;

import org.springframework.stereotype.Component;
import ru.test.core.model.dto.ImageResponse;
import ru.test.core.model.entity.Image;


@Component
public class ImageMapper {

    public ImageResponse toDto(Image image) {
        if (image == null) {
            return null;
        }

        return new ImageResponse(
                image.getId(),
                image.getFileName(),
                image.getSize(),
                image.getUploadDate(),
                image.getUrl()
        );
    }

    public Image toEntity(ImageResponse imageResponse) {
        if (imageResponse == null) {
            return null;
        }

        Image image = new Image();
        image.setId(imageResponse.getId());
        image.setFileName(imageResponse.getFileName());
        image.setSize(imageResponse.getSize());
        image.setUploadDate(imageResponse.getUploadDate());
        image.setUrl(imageResponse.getUrl());

        return image;
    }
}
