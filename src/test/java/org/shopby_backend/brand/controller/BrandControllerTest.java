package org.shopby_backend.brand.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.shopby_backend.brand.dto.BrandFilter;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.service.BrandService;
import org.shopby_backend.exception.brand.BrandAlreadyExistsException;
import org.shopby_backend.exception.brand.BrandNotFoundException;
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
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BrandController.class)
@AutoConfigureMockMvc(addFilters = false)
class BrandControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BrandService brandService;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    JwtFilterService jwtFilterService;

    @Test
    @WithMockUser(authorities = "BRAND_CREATE")
    void shouldAddBrand() throws Exception {
        BrandInputDto brandInputDto= new BrandInputDto("DC");
        BrandOutputDto brandOutputDto=new BrandOutputDto(1L,brandInputDto.libelle());
        when(brandService.addBrand(any())).thenReturn(brandOutputDto);

        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(brandInputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "BRAND_CREATE")
    void shouldThrownAnExceptionWhenAlreadyExistAddBrand() throws Exception {
        BrandInputDto brandInputDto= new BrandInputDto("DC");
        when(brandService.addBrand(any())).thenThrow(new BrandAlreadyExistsException(brandInputDto.libelle()));

        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(brandInputDto)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(authorities = "BRAND_UPDATE")
    void shouldUpdateBrand() throws Exception {
        BrandInputDto brandInputDto= new BrandInputDto("DC");
        BrandOutputDto brandOutputDto=new BrandOutputDto(1L,brandInputDto.libelle());
        when(brandService.updateBrand(any(Long.class),any(BrandInputDto.class))).thenReturn(brandOutputDto);

        mockMvc.perform(patch("/brand/{brandId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(brandInputDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "BRAND_UPDATE")
    void shouldThrowNotFoundWhenBrandNotFoundUpdateBrand() throws Exception {
        BrandInputDto brandInputDto= new BrandInputDto("DC");
        BrandOutputDto brandOutputDto=new BrandOutputDto(1L,brandInputDto.libelle());
        when(brandService.updateBrand(any(Long.class),any(BrandInputDto.class))).thenThrow(new BrandNotFoundException(brandOutputDto.id()));

        mockMvc.perform(patch("/brand/{brandId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(brandInputDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "BRAND_READ_ALL")
    void shouldReturnAllBrands() throws Exception {
        BrandFilter brandFilter = new BrandFilter("DC");
        List<BrandOutputDto> brandOutputDto= Collections.singletonList(new BrandOutputDto(1L, brandFilter.libelle()));
        Page<BrandOutputDto> pageBrandOutputDto=new PageImpl<>(brandOutputDto);
        when(brandService.findAllBrands(any(BrandFilter.class),any(Pageable.class))).thenReturn(pageBrandOutputDto);

        mockMvc.perform(get("/brand")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "BRAND_READ")
    void shouldReturnBrandsById() throws Exception {
        BrandOutputDto brandOutputDto = new BrandOutputDto(1L,"DC");
        when(brandService.findBrandById(any(Long.class))).thenReturn(brandOutputDto);

        mockMvc.perform(get("/brand/{brandId}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "BRAND_READ")
    void shouldThrownNotFoundWhenReturnBrandsById() throws Exception {
        when(brandService.findBrandById(any(Long.class))).thenThrow(new BrandNotFoundException(1L));

        mockMvc.perform(get("/brand/{brandId}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "BRAND_DELETE")
    void shouldDeleteBrand() throws Exception {
        mockMvc.perform(delete("/brand/{brandId}",1L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "BRAND_DELETE")
    void shouldThrownNotFoundExceptionWhenDeleteBrand() throws Exception {
        doThrow(new BrandNotFoundException(1L))
                .when(brandService)
                .deleteBrand(any(Long.class));

        mockMvc.perform(delete("/brand/{brandId}",1L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }



















}