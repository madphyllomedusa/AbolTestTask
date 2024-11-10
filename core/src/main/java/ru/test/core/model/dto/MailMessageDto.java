package ru.test.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessageDto {
    private String email;
    private String username;
    private String totalSize;
    private String filename;
    private String fileSize;
}
