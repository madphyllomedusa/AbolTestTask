package ru.test.core.service.Impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.test.core.model.dto.MailMessageDto;
import ru.test.core.service.KafkaProducerService;

@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerServiceImpl.class);
    private final KafkaTemplate<String, MailMessageDto> kafkaTemplate;

    @Value("${kafka.topics.userWelcome}")
    private String userWelcomeTopic;

    @Value("${kafka.topics.imageUpload}")
    private String imageUploadTopic;

    @Value("${kafka.topics.imageDownload}")
    private String imageDownloadTopic;

    @Override
    public void sendWelcomeEmail(String email, String username) {
        MailMessageDto message = new MailMessageDto();
        message.setEmail(email);
        message.setUsername(username);
        logger.info("Sending welcome email: {}", message);
        kafkaTemplate.send(userWelcomeTopic, message);
    }

    @Override
    public void sendImageUploadNotification(String email, String username, String totalSize) {
        MailMessageDto message = new MailMessageDto();
        message.setEmail(email);
        message.setUsername(username);
        message.setTotalSize(totalSize);
        logger.info("Sending image upload notification: {}", message);
        kafkaTemplate.send(imageUploadTopic, message);

    }

    @Override
    public void sendImageDownloadNotification(String email, String fileName, String fileSize) {
        MailMessageDto message = new MailMessageDto();
        message.setEmail(email);
        message.setFilename(fileName);
        message.setFileSize(fileSize);
        logger.info("Sending image download notification: {}", message);
        kafkaTemplate.send(imageDownloadTopic, message);
    }
}
