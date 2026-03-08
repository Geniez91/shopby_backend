package org.shopby_backend.wishlist.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.shopby_backend.article.controller.ArticleController;
import org.shopby_backend.article.service.ArticleService;
import org.shopby_backend.exception.wishlist.WishlistNotFoundException;
import org.shopby_backend.jwt.service.JwtFilterService;
import org.shopby_backend.jwt.service.JwtService;
import org.shopby_backend.wishlist.dto.WishlistInputDto;
import org.shopby_backend.wishlist.dto.WishlistOutputDto;
import org.shopby_backend.wishlist.dto.WishlistUpdateDto;
import org.shopby_backend.wishlist.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WishlistController.class)
@AutoConfigureMockMvc(addFilters = false)
class WishlistControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    WishlistService wishlistService;

    @MockitoBean
    ArticleService articleService;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtFilterService jwtFilterService;

    @Test
    void shouldAddNewWishlist() throws Exception {
        WishlistInputDto wishlistInputDto = new WishlistInputDto(1L,"test","description");
        WishlistOutputDto wishlistOutputDto = new WishlistOutputDto(1L,wishlistInputDto.userId(),wishlistInputDto.name(),wishlistInputDto.description(),1L);
        when(wishlistService.addWishList(any(WishlistInputDto.class))).thenReturn(wishlistOutputDto);

        mockMvc.perform(post("/wishlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(wishlistInputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateWishlist() throws Exception {
        WishlistUpdateDto wishlistUpdateDto = new WishlistUpdateDto("test","description");
        WishlistOutputDto wishlistOutputDto = new WishlistOutputDto(1L,1L,wishlistUpdateDto.name(),wishlistUpdateDto.description(),1L);
        when(wishlistService.updateWishlist(any(Integer.class),any(WishlistUpdateDto.class))).thenReturn(wishlistOutputDto);

        mockMvc.perform(patch("/wishlist/{wishlistId}",1L).
                contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(wishlistUpdateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrownNotFoundWishlistException() throws Exception {
        WishlistUpdateDto wishlistUpdateDto = new WishlistUpdateDto("test","description");

        when(wishlistService.updateWishlist(any(Integer.class),any(WishlistUpdateDto.class))).thenThrow(new WishlistNotFoundException(1));

        mockMvc.perform(patch("/wishlist/{wishlistId}",1L).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wishlistUpdateDto)))
                .andExpect(status().isNotFound());
    }



}