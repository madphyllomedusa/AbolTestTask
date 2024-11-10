package ru.test.mail.service;

public interface MailService {
    void sendWelcomeEmail(String email, String username);
    void sendImageUploadNotification(String email, String username, String totalSize);
    void sendImageDownloadNotification(String email, String filename, String fileSize);
}
