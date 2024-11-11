package ru.test.mail.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.test.mail.dto.MailMessageDto;
import ru.test.mail.service.MailService;

@Component
@RequiredArgsConstructor
public class KafkaMailEventListener {
    private final MailService mailService;

    @KafkaListener(topics = "user-welcome-topic", containerFactory = "kafkaListenerContainerFactory")
    public void handleUserRegistration(MailMessageDto message) {
        mailService.sendWelcomeEmail(
                message.getEmail(),
                message.getUsername());
    }

    @KafkaListener(topics = "image-upload-topic", containerFactory = "kafkaListenerContainerFactory")
    public void handleImageUpload(MailMessageDto message) {
        mailService.sendImageUploadNotification(
                message.getEmail(),
                message.getUsername(),
                message.getTotalSize());
    }

    @KafkaListener(topics = "image-download-topic", containerFactory = "kafkaListenerContainerFactory")
    public void handleImageDownload(MailMessageDto message) {
        mailService.sendImageDownloadNotification(
                message.getEmail(),
                message.getFilename(),
                message.getFileSize());
    }
}
