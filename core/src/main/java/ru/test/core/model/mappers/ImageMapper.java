package ru.test.core.model.mappers;

import org.springframework.stereotype.Component;
import ru.test.core.model.dto.ImageResponse;
import ru.test.core.model.entity.Image;


@Component
public class ImageMapper {

    public ImageResponse toDto(Image image) {
      ImageResponse imageResponse = new ImageResponse();
      imageResponse.setId(image.getId());
      imageResponse.setFileName(image.getFileName());
      imageResponse.setSize(image.getSize());
      imageResponse.setUploadDate(image.getUploadDate());
      imageResponse.setUrl(image.getUrl());
      imageResponse.setUserId(image.getUser().getId());
      return imageResponse;

    }

}
