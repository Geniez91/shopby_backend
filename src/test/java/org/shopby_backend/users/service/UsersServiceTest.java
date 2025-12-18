package org.shopby_backend.users.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shopby_backend.exception.users.UsersCreateException;
import org.shopby_backend.users.dto.UserInputDto;
import org.shopby_backend.users.dto.UsersDto;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {
    @Mock
    UsersRepository usersRepository;

    @Mock
    ValidationService validationService;

    /// Permet d'injecter les mocks de repository dans le service
    @InjectMocks
    UsersService usersService;


    @BeforeEach
    void setUp() {
        usersService = new UsersService(usersRepository, new BCryptPasswordEncoder(),validationService);
    }

    @Test
    void shouldAddUser() {
        /// Arrange
        UserInputDto inputDto = new UserInputDto("Jeremy", "Weltmann", "test123", "jeremy@example.com");
        UsersEntity user = UsersEntity.builder().
                id(1L)
                .nom(inputDto.nom())
                .prenom(inputDto.prenom())
                .email(inputDto.email())
                .password(inputDto.password())
                .build();
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(user);

        /// Act
        UsersDto result= usersService.addUser(inputDto);

        /// Assert
        assertEquals(user.getEmail(),result.email());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        UserInputDto inputDto = new UserInputDto("Jeremy", "Weltmann", "test123", "jeremy@example.com");
        UsersEntity existingUser = UsersEntity.builder().
                id(1L)
                .nom(inputDto.nom())
                .prenom(inputDto.prenom())
                .email(inputDto.email())
                .password(inputDto.password())
                .build();
        when(usersRepository.findByEmail(inputDto.email())).thenReturn(existingUser);

        UsersCreateException usersCreateException= Assertions.assertThrows(
                UsersCreateException.class,
                () -> usersService.addUser(inputDto)
        );

        Assertions.assertEquals("Vos identifiants existe deja", usersCreateException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailMalformatted() {
        UserInputDto inputDto = new UserInputDto("Jeremy", "Weltmann", "test123", "jeremyexample.com");

        UsersCreateException usersCreateException= Assertions.assertThrows(
                UsersCreateException.class,
                () -> usersService.addUser(inputDto)
        );

        Assertions.assertEquals("Votre email est invalide", usersCreateException.getMessage());
    }
}