package ru.test.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    private Long userId;
    private Long id;
    private String fileName;
    private Long size;
    private OffsetDateTime uploadDate;
    private String url;
}
