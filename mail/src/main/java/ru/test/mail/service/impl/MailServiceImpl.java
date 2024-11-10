package ru.test.mail.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        String title = "Регистрация";
        String text = String.format("%s, спасибо что прошли регистрацию!", username);
        logger.info("trying to send message on email");
        sendEmail(email, title, text);
    }

    @Override
    public void sendImageUploadNotification(String email, String username, String totalSize) {
        String title = "Загрузка файлов";
        String text = String.format("Привет %s, вы успешно загрузили изображений на %s.", username, totalSize);

        sendEmail(email, title, text);
    }

    @Override
    public void sendImageDownloadNotification(String email, String filename, String fileSize) {
        String title = "Скачивание файлов";
        String text = String.format("Вы успешно скачали: %s (размером: %s).", filename, fileSize);

        sendEmail(email, title, text);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
