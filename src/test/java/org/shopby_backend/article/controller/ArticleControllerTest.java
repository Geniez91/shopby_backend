package org.shopby_backend.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.dto.ArticleFilter;
import org.shopby_backend.article.service.ArticleService;
import org.shopby_backend.brand.controller.BrandController;
import org.shopby_backend.brand.service.BrandService;
import org.shopby_backend.exception.article.ArticleAlreadyExistsException;
import org.shopby_backend.exception.article.ArticleNotFoundException;
import org.shopby_backend.jwt.service.JwtFilterService;
import org.shopby_backend.jwt.service.JwtService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArticleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ArticleService articleService;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtFilterService jwtFilterService;

    @WithMockUser(authorities = "ARTICLE_CREATE")
    @Test
    void shouldAddArticle() throws Exception{
        AddArticleInputDto addArticleInputDto = new AddArticleInputDto("name","description", BigDecimal.ONE,1L,1L);
        AddArticleOutputDto addArticleOutputDto = new AddArticleOutputDto(1L,addArticleInputDto.nameArticle(),addArticleInputDto.descriptionArticle(),addArticleInputDto.price(),"DC","Comics",new Date(),1L,3.5,2L);
        when(articleService.addNewArticle(any(AddArticleInputDto.class))).thenReturn(addArticleOutputDto);

        mockMvc.perform(post("/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addArticleInputDto)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(authorities = "ARTICLE_CREATE")
    @Test
    void shouldThrowAlreadyExceptionWhenAddArticle() throws Exception{
        AddArticleInputDto addArticleInputDto = new AddArticleInputDto("name","description", BigDecimal.ONE,1L,1L);
        when(articleService.addNewArticle(any(AddArticleInputDto.class))).thenThrow(new ArticleAlreadyExistsException(addArticleInputDto.nameArticle()));

        mockMvc.perform(post("/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addArticleInputDto)))
                .andExpect(status().isConflict());
    }

    @WithMockUser(authorities = "ARTICLE_UPDATE")
    @Test
    void shouldUpdateArticle() throws Exception{
        AddArticleInputDto addArticleInputDto = new AddArticleInputDto("name","description", BigDecimal.ONE,1L,1L);
        AddArticleOutputDto addArticleOutputDto = new AddArticleOutputDto(1L,addArticleInputDto.nameArticle(),addArticleInputDto.descriptionArticle(),addArticleInputDto.price(),"DC","Comics",new Date(),1L,3.5,2L);
        when(articleService.updateArticle(any(Long.class), any(AddArticleInputDto.class))).thenReturn(addArticleOutputDto);

        mockMvc.perform((patch("/article/{id}",1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addArticleInputDto)))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = "ARTICLE_UPDATE")
    @Test
    void shouldThrownNotFoundExceptionWhenUpdateArticle() throws Exception{
        AddArticleInputDto addArticleInputDto = new AddArticleInputDto("name","description", BigDecimal.ONE,1L,1L);
        when(articleService.updateArticle(any(Long.class), any(AddArticleInputDto.class))).thenThrow(new ArticleNotFoundException(1L));

        mockMvc.perform((patch("/article/{id}",1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addArticleInputDto)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(authorities = "ARTICLE_DELETE")
    @Test
    void shouldDeleteArticle() throws Exception{
        mockMvc.perform((delete("/article/{id}",1L)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @WithMockUser(authorities = "ARTICLE_DELETE")
    @Test
    void shouldThrownNotFoundDeleteArticle() throws Exception{
        doThrow(new ArticleNotFoundException(1L)).when(articleService).deleteArticle(1L);

        mockMvc.perform((delete("/article/{id}",1L)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllArticles() throws Exception{
        List<AddArticleOutputDto> addArticleOutputDto = Collections.singletonList(new AddArticleOutputDto(1L,"name","description", BigDecimal.ONE,"DC","Comics",new Date(),1L,5.5,1L));
        Page<AddArticleOutputDto> page = new PageImpl<>(addArticleOutputDto);
        when(articleService.getFilteredArticles(any(ArticleFilter.class),any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/article").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void shouldGetArticleById() throws Exception{
        AddArticleOutputDto addArticleOutputDto = new AddArticleOutputDto(1L,"name","description", BigDecimal.ONE,"DC","Comics",new Date(),1L,5.5,1L);
        when(articleService.getArticleById(any(Long.class))).thenReturn(addArticleOutputDto);

        mockMvc.perform(get("/article/{id}",1L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }







}