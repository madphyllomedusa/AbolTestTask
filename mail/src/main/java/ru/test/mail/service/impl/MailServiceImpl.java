package ru.test.mail.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.test.mail.service.MailService;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Override
    public void sendWelcomeEmail(String email, String username) {
        logger.info("Attempting to send welcome email to {}", email);
        String title = "Регистрация";
        String text = String.format("%s, спасибо, что прошли регистрацию!", username);
        sendEmail(email, title, text);
    }

    @Override
    public void sendImageUploadNotification(String email, String username, String totalSize) {
        logger.info("Attempting to send image upload notification to {}", email);
        String title = "Загрузка файлов";
        String text = String.format("Привет %s, вы успешно загрузили изображения общим размером %s.", username, totalSize);

        sendEmail(email, title, text);
    }

    @Override
    public void sendImageDownloadNotification(String email, String filename, String fileSize) {
        logger.info("Attempting to send image download notification to {}", email);
        String title = "Скачивание файлов";
        String text = String.format("Вы успешно скачали файл: %s (размер: %s).", filename, fileSize);

        sendEmail(email, title, text);
    }


    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            logger.info("Email successfully sent to {}", to);
        } catch (MailSendException ex) {
            logger.error("Failed to send email to {} due to invalid email address: {}", to, ex.getMessage());
        } catch (MailException ex) {
            logger.error("Failed to send email to {} due to unexpected error: {}", to, ex.getMessage());
        }
    }
}
