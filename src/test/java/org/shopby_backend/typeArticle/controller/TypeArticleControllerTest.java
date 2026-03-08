package org.shopby_backend.typeArticle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.shopby_backend.exception.typeArticle.TypeArticleAlreadyExistsException;
import org.shopby_backend.exception.typeArticle.TypeArticleNotFoundException;
import org.shopby_backend.jwt.service.JwtFilterService;
import org.shopby_backend.jwt.service.JwtService;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleFilter;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.service.TypeArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TypeArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
class TypeArticleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TypeArticleService typeArticleService;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtFilterService jwtFilterService;

    @WithMockUser(authorities = "TYPE_ARTICLE_CREATE")
    @Test
    void shouldAddTypeArticle() throws Exception {
        TypeArticleDto typeArticleDto = new TypeArticleDto("test",2L);
        TypeArticleOutputDto typeArticleOutputDto = new TypeArticleOutputDto(1L,"Comics",2L);
        when(typeArticleService.addTypeArticle(any(TypeArticleDto.class))).thenReturn(typeArticleOutputDto);

        mockMvc.perform(post("/type_article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(typeArticleDto)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(authorities = "TYPE_ARTICLE_CREATE")
    @Test
    void shouldThrownAlreadyExceptionAddTypeArticle() throws Exception {
        TypeArticleDto typeArticleDto = new TypeArticleDto("test",2L);
        when(typeArticleService.addTypeArticle(any(TypeArticleDto.class))).thenThrow(new TypeArticleAlreadyExistsException(typeArticleDto.libelle()));

        mockMvc.perform(post("/type_article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(typeArticleDto)))
                .andExpect(status().isConflict());
    }

    @WithMockUser(authorities = "TYPE_ARTICLE_UPDATE")
    @Test
    void shouldUpdateTypeArticle() throws Exception {
        TypeArticleDto typeArticleDto = new TypeArticleDto("test",2L);
        TypeArticleOutputDto typeArticleOutputDto = new TypeArticleOutputDto(1L,"Comics",2L);
        when(typeArticleService.updateTypeArticle(any(Long.class),any(TypeArticleDto.class))).thenReturn(typeArticleOutputDto);

        mockMvc.perform(patch("/type_article/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(typeArticleDto)))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = "TYPE_ARTICLE_UPDATE")
    @Test
    void shouldThrownNotFoundTypeArticle() throws Exception {
        TypeArticleDto typeArticleDto = new TypeArticleDto("test",2L);
        when(typeArticleService.updateTypeArticle(any(Long.class),any(TypeArticleDto.class))).thenThrow(TypeArticleNotFoundException.byId(1L));

        mockMvc.perform(patch("/type_article/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(typeArticleDto))).andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(authorities = "TYPE_ARTICLE_DELETE")
    @Test
    void shouldDeleteTypeArticle() throws Exception {
        mockMvc.perform(delete("/type_article/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(1L)))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(authorities = "TYPE_ARTICLE_DELETE")
    @Test
    void shouldThrownNotFoundExceptionWhenDeleteTypeArticle() throws Exception {
        doThrow(TypeArticleNotFoundException.byId(1L)).when(typeArticleService).deleteTypeArticle(any(Long.class));

        mockMvc.perform(delete("/type_article/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(authorities = "TYPE_ARTICLE_READ_ALL")
    @Test
    void shouldReturnAllTypeArticle()  throws Exception {
        List<TypeArticleOutputDto> typeArticleDtoList = Collections.singletonList(new TypeArticleOutputDto(1L,"Comics",2L));
        Page<TypeArticleOutputDto> page = new PageImpl<>(typeArticleDtoList);
        when(typeArticleService.getAllTypeArticle(any(TypeArticleFilter.class),any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/type_article").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @WithMockUser(authorities = "TYPE_ARTICLE_READ")
    @Test
    void shouldReturnTypeArticleById() throws Exception {
        TypeArticleOutputDto typeArticleOutputDto = new TypeArticleOutputDto(1L,"Comics",2L);
        when(typeArticleService.getTypeArticleById(any(Long.class))).thenReturn(typeArticleOutputDto);

        mockMvc.perform(get("/type_article/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }











}