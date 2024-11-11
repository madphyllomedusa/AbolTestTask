package ru.test.core.service;



public interface KafkaProducerService {

    void sendWelcomeEmail(String email, String username);

    void sendImageUploadNotification(String email, String username, String totalSize);

    void sendImageDownloadNotification(String email, String fileName, String fileSize);
}
