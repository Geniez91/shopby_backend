package org.shopby_backend.users.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.users.model.ValidationEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class NotificationService {
    private final JavaMailSender mailSender;

    public void sendRegistrationConfirmation(ValidationEntity validationEntity) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("weltmannjeremy@gmail.com");
        mailMessage.setTo(validationEntity.getUser().getEmail());
        String text=String.format("Bonjour %s, <br> Votre Code d'activation est %s",validationEntity.getUser().getUsername(),validationEntity.getCode());
        mailMessage.setText(text);
        mailMessage.setSubject("Votre Code d'activation");

        mailSender.send(mailMessage);
    }
}
