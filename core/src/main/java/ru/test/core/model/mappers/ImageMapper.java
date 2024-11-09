package ru.test.core.model.mappers;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.test.core.model.dto.ImageResponse;
import ru.test.core.model.entity.Image;
import ru.test.core.model.entity.User;

import java.time.OffsetDateTime;


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

    public Image createImage(MultipartFile file, User user, String url) {
    Image image = new Image();
    image.setFileName(file.getOriginalFilename());
    image.setSize(file.getSize());
    image.setUploadDate(OffsetDateTime.now());
    image.setUser(user);
    image.setUrl(url);
    return image;
}


}
