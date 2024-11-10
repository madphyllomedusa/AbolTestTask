package ru.test.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.test.core.model.dto.MailMessageDto;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, MailMessageDto> kafkaTemplate;

    public void sendWelcomeEmail(MailMessageDto message) {
        kafkaTemplate.send("user-welcome-topic", message);
    }

    public void sendImageUploadNotification(MailMessageDto message) {
        kafkaTemplate.send("image-upload-topic", message);
    }

    public void sendImageDownloadNotification(MailMessageDto message) {
        kafkaTemplate.send("image-download-topic", message);
    }
}
