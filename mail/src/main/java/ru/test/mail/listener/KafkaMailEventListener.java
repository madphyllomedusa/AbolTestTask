package ru.test.mail.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.test.mail.dto.MailMessageDto;
import ru.test.mail.service.MailService;

@Component
@RequiredArgsConstructor
public class KafkaMailEventListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaMailEventListener.class);
    private final MailService mailService;

    @KafkaListener(topics = "${kafka.topics.userWelcome}", containerFactory = "kafkaListenerContainerFactory")
    public void handleUserRegistration(MailMessageDto message) {
        if (message == null || message.getEmail() == null || message.getUsername() == null) {
            logger.warn("Received null or incomplete user registration message: {}", message);
            return;
        }
        logger.info("Processing user registration for email: {}, username: {}", message.getEmail(), message.getUsername());
        mailService.sendWelcomeEmail(
                message.getEmail(),
                message.getUsername());
    }

    @KafkaListener(topics = "${kafka.topics.imageUpload}", containerFactory = "kafkaListenerContainerFactory")
    public void handleImageUpload(MailMessageDto message) {
        if (message == null || message.getEmail() == null || message.getUsername() == null || message.getTotalSize() == null) {
            logger.warn("Received null or incomplete image upload message: {}", message);
            return;
        }
        logger.info("Processing image upload notification for user: {}, total size: {}", message.getUsername(), message.getTotalSize());
        mailService.sendImageUploadNotification(
                message.getEmail(),
                message.getUsername(),
                message.getTotalSize());
    }

    @KafkaListener(topics = "${kafka.topics.imageDownload}", containerFactory = "kafkaListenerContainerFactory")
    public void handleImageDownload(MailMessageDto message) {
        if (message == null || message.getEmail() == null || message.getFilename() == null || message.getFileSize() == null) {
            logger.warn("Received null or incomplete image download message: {}", message);
            return;
        }
        logger.info("Processing image download notification for file: {}, size: {}, user email: {}",
                message.getFilename(), message.getFileSize(), message.getEmail());
        mailService.sendImageDownloadNotification(
                message.getEmail(),
                message.getFilename(),
                message.getFileSize());
    }
}
