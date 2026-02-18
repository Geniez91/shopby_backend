package org.shopby_backend.users.mapper;

import org.shopby_backend.users.dto.UserInputDto;
import org.shopby_backend.users.dto.UsersDto;
import org.shopby_backend.users.model.RoleEntity;
import org.shopby_backend.users.model.UsersEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {
    public UsersEntity toEntity(UserInputDto userInputDto, BCryptPasswordEncoder bCryptPasswordEncoder, RoleEntity roleEntity) {
        return UsersEntity.builder()
                .nom(userInputDto.nom())
                .prenom(userInputDto.prenom())
                .password(bCryptPasswordEncoder.encode(userInputDto.password()))
                .email(userInputDto.email())
                .role(roleEntity)
                .country(userInputDto.country())
                .enabled(false)
                .billingAddress(null)
                .deliveryAddress(null)
                .build();
    }

    public UsersDto toDto(UsersEntity usersEntity) {
       return new UsersDto(
               usersEntity.getId(),
               usersEntity.getNom(),
               usersEntity.getPrenom(),
               usersEntity.getPassword(),
               usersEntity.getEmail(),
               usersEntity.getCountry(),
               usersEntity.getDeliveryAddress(),
               usersEntity.getBillingAddress());
    }
    }

