package org.shopby_backend.users.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.exception.users.ValidationAccountException;
import org.shopby_backend.exception.users.ValidationNotFoundException;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.users.mapper.ValidationMapper;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.model.ValidationEntity;
import org.shopby_backend.users.persistence.ValidationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Slf4j
@AllArgsConstructor
@Service
public class ValidationService {
    public static final int BOUND = 99999;
    private ValidationRepository validationRepository;
    private NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);
    private ValidationMapper validationMapper;

    public void save(UsersEntity user) {
        long start = System.nanoTime();
        Instant creationDate = Instant.now();

        Random random = new Random();
        random.nextInt(BOUND);
        String code= String.format("%06d", random.nextInt(BOUND));

        ValidationEntity validationEntity=validationMapper.toEntity(user,creationDate,code);
        validationRepository.save(validationEntity);
        notificationService.sendRegistrationConfirmation(validationEntity);
        long durationMs = Tools.getDurationMs(start);;
        logger.info("La validation de l'utilisateur {} a bien été effectué,durationMs = {}",code,durationMs);
    }

    public ValidationEntity readCode(String code){
        long start = System.nanoTime();
        ValidationEntity validation=validationRepository.findByCode(code).orElseThrow(()->{
            ValidationNotFoundException exception = new ValidationNotFoundException(code);
            logger.error(LogMessages.VALIDATION_NOT_FOUND_BY_CODE,code);
            return exception;
        });
        long durationMs = Tools.getDurationMs(start);;
        logger.info("Le code {} est bien présent dans la base de données, durationMs = {}",code,durationMs);
        return validation;
    }
}
