package org.shopby_backend.users.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.exception.users.ValidationAccountException;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.model.ValidationEntity;
import org.shopby_backend.users.persistence.ValidationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@AllArgsConstructor
@Service
public class ValidationService {
    public static final int BOUND = 99999;
    private ValidationRepository validationRepository;
    private NotificationService notificationService;

    public void save(UsersEntity user) {
        Instant creationDate = Instant.now();

        Random random = new Random();
        random.nextInt(BOUND);
        String code= String.format("%06d", random.nextInt(BOUND));

        ValidationEntity validationEntity=ValidationEntity.builder()
                .user(user)
                .creationDate(creationDate)
                .expirationDate(creationDate.plus(10, ChronoUnit.MINUTES))
                .code(code)
                .build();
        validationRepository.save(validationEntity);
        notificationService.sendRegistrationConfirmation(validationEntity);
    }

    public ValidationEntity readCode(String code){
        ValidationEntity validation=validationRepository.findByCode(code);
        if(validation==null){
            throw new ValidationAccountException("Le code d'action n'est pas valide");
        }
        return validation;
    }
}
