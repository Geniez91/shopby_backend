package org.shopby_backend.users.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.model.ValidationEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void sendMailToUser() {
        UsersEntity user = UsersEntity.builder().email("weltmannjeremy@gmail.com").build();
        ValidationEntity validationEntity=ValidationEntity.builder().user(user).code("123456").build();

        notificationService.sendRegistrationConfirmation(validationEntity);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage message=messageCaptor.getValue();
        assertEquals("weltmannjeremy@gmail.com",message.getFrom());
        assertNotNull(message.getTo());
        assertEquals("weltmannjeremy@gmail.com",message.getTo()[0]);
        assertEquals("Votre Code d'activation",message.getSubject());
    }
}