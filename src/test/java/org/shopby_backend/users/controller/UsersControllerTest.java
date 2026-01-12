package org.shopby_backend.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.shopby_backend.jwt.service.JwtService;
import org.shopby_backend.users.dto.UserInfoUpdateDto;
import org.shopby_backend.users.dto.UserInputDto;
import org.shopby_backend.users.dto.UserOutputInfoUpdateDto;
import org.shopby_backend.users.dto.UsersDto;
import org.shopby_backend.users.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.authentication.AuthenticationManager;

@WebMvcTest(UsersController.class)
@ContextConfiguration(classes = UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UsersService usersService;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @MockitoBean
    JwtService jwtService;

    @Test
    void shouldAddUser() throws Exception {
        /// Arrange
        UserInputDto inputDto = new UserInputDto("Jeremy", "Weltmann", "test123", "jeremy@example.com","France");
        UsersDto user = new UsersDto(1L,inputDto.nom(),inputDto.prenom(),inputDto.email(),inputDto.password(), inputDto.country());
        when(usersService.addUser(inputDto)).thenReturn(user);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(inputDto)))
                .andExpect(status().isOk());

    }

    @Test
    void shouldUpdateUserInfo() throws Exception {
        ///Arrange
        UserInfoUpdateDto userInfoUpdateDto=new UserInfoUpdateDto("Jeremy","Weltmann","123","weltmannjeremy@gmail.com","France","10 rue des Tulipes","10 rue des Tulipes");
        UserOutputInfoUpdateDto user=new UserOutputInfoUpdateDto(1L,userInfoUpdateDto.prenom(),userInfoUpdateDto.nom(),userInfoUpdateDto.password(),userInfoUpdateDto.email(),userInfoUpdateDto.country(),userInfoUpdateDto.deliveryAddress(),userInfoUpdateDto.billingAddress());
        when(usersService.updateUserInfo(1L,userInfoUpdateDto)).thenReturn(user);

        mockMvc.perform(patch("/user/{userId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userInfoUpdateDto)))
                .andExpect(status().isOk());
    }
}