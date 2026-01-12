package org.shopby_backend.users.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shopby_backend.exception.users.UsersCreateException;
import org.shopby_backend.exception.users.UsersUpdateException;
import org.shopby_backend.users.dto.UserInfoUpdateDto;
import org.shopby_backend.users.dto.UserInputDto;
import org.shopby_backend.users.dto.UserOutputInfoUpdateDto;
import org.shopby_backend.users.dto.UsersDto;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {
    @Mock
    UsersRepository usersRepository;

    @Mock
    ValidationService validationService;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UsersService usersService;


    @BeforeEach
    void setUp() {
        usersService = new UsersService(usersRepository, new BCryptPasswordEncoder(),validationService);
    }

    @Test
    void shouldAddUser() {
        /// Arrange
        UserInputDto inputDto = new UserInputDto("Jeremy", "Weltmann", "test123", "jeremy@example.com","France");
        UsersEntity user = UsersEntity.builder().
                id(1L)
                .nom(inputDto.nom())
                .prenom(inputDto.prenom())
                .email(inputDto.email())
                .password(inputDto.password())
                .country(inputDto.country())
                .build();
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(user);

        /// Act
        UsersDto result= usersService.addUser(inputDto);

        /// Assert
        assertEquals(user.getEmail(),result.email());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        UserInputDto inputDto = new UserInputDto("Jeremy", "Weltmann", "test123", "jeremy@example.com","France");
        UsersEntity existingUser = UsersEntity.builder().
                id(1L)
                .nom(inputDto.nom())
                .prenom(inputDto.prenom())
                .email(inputDto.email())
                .password(inputDto.password())
                .country(inputDto.country())
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
        UserInputDto inputDto = new UserInputDto("Jeremy", "Weltmann", "test123", "jeremyexample.com","France");

        UsersCreateException usersCreateException= Assertions.assertThrows(
                UsersCreateException.class,
                () -> usersService.addUser(inputDto)
        );

        Assertions.assertEquals("Votre email est invalide", usersCreateException.getMessage());
    }

    @Test
    void shouldUpdateUserInfo(){
        /// Arrange
        Long idUser=0L;
        UserInfoUpdateDto userInfoUpdate=new UserInfoUpdateDto("Jeremy","Weltmann","123","weltmannjeremy@gmail.com","France","10 rue des Tulipes","10 rue des Tulipes");
        UsersEntity oldUser=UsersEntity.builder()
                .id(idUser)
                .country("France")
                .nom("Jeremy1")
                .prenom("Weltmann")
                .deliveryAddress("10 rue des Roses")
                .billingAddress("10 rue des Roses")
                .email("weltmannjeremy@gmail.com")
                .password("123")
                .build();

        UsersEntity user=UsersEntity.builder()
                .id(idUser)
                .country("France")
                .nom("Jeremy")
                .prenom("Weltmann")
                .deliveryAddress("10 rue des Tulipes")
                .billingAddress("10 rue des Tulipes")
                .email("weltmannjeremy@gmail.com")
                .password("123")
                .build();
        doReturn(Optional.of(oldUser))
                .when(usersRepository)
                .findById(idUser);

        when(usersRepository.save(any(UsersEntity.class))).thenReturn(user);
        /// Act
        UserOutputInfoUpdateDto result= usersService.updateUserInfo(idUser,userInfoUpdate);
        /// Assert
        assertEquals(user.getEmail(),result.getEmail());
        assertEquals(user.getId(),result.getId());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundUpdateUserInfo(){
        Long idUser=0L;
        UserInfoUpdateDto userInfoUpdate=new UserInfoUpdateDto("Jeremy","Weltmann","123","weltmannjeremy@gmail.com","France","10 rue des Tulipes","10 rue des Tulipes");
        doReturn(Optional.empty())
                .when(usersRepository)
                .findById(idUser);

        UsersUpdateException usersUpdateException= Assertions.assertThrows(
                UsersUpdateException.class,
                () -> usersService.updateUserInfo(idUser,userInfoUpdate)
        );

        Assertions.assertEquals("L'utilisateur n'existe pas", usersUpdateException.getMessage());
    }

    @Test
    void shouldReturnUpdatedUserInfoWhenOnlyPrenomUpdated(){
        Long idUser=0L;
        String prenom="Jeremy";
        UserInfoUpdateDto userInfoUpdate=new UserInfoUpdateDto(prenom,null,null,null,null,null,null);
        UsersEntity oldUser=UsersEntity.builder()
                .id(idUser)
                .country("France")
                .nom("Weltmann")
                .prenom("Jeremy1")
                .deliveryAddress("10 rue des Roses")
                .billingAddress("10 rue des Roses")
                .email("weltmannjeremy@gmail.com")
                .password("123")
                .build();
        UsersEntity user=UsersEntity.builder()
                .id(idUser)
                .country("France")
                .nom("Weltmann")
                .prenom("Jeremy")
                .deliveryAddress("10 rue des Roses")
                .billingAddress("10 rue des Roses")
                .email("weltmannjeremy@gmail.com")
                .password("123")
                .build();
        when(usersRepository.findById(idUser)).thenReturn(Optional.of(oldUser));
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(user);

        UserOutputInfoUpdateDto result= usersService.updateUserInfo(idUser,userInfoUpdate);

        assertEquals(prenom,result.getPrenom());
        assertEquals(user.getId(),result.getId());
    }

}