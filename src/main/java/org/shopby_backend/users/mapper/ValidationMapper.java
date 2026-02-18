package org.shopby_backend.users.mapper;

import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.model.ValidationEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ValidationMapper {
    public ValidationEntity toEntity(UsersEntity usersEntity, Instant creationDate,String code){
        return ValidationEntity.builder()
                .user(usersEntity)
                .creationDate(creationDate)
                .expirationDate(creationDate.plus(10, ChronoUnit.MINUTES))
                .code(code)
                .build();
    }
}
