package org.shopby_backend.brand.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.shopby_backend.exception.brand.*;
import org.shopby_backend.users.model.UsersEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    @Mock
    BrandRepository brandRepository;

    @InjectMocks
    BrandService brandService;

    @Test
    void shoudlAddBrand(){
        /// Arrange
        BrandInputDto brandInputDto =new BrandInputDto("DC");
        BrandEntity brandEntity=BrandEntity.builder()
                .idBrand(1L)
                .libelle(brandInputDto.libelle())
                .build();
        when(brandRepository.existsByLibelle(brandInputDto.libelle())).thenReturn(false);
        when(brandRepository.save(any(BrandEntity.class))).thenReturn(brandEntity);
        /// Act
        BrandOutputDto addBrandOutputDto= brandService.addBrand(brandInputDto);

        /// Asserts
        Assertions.assertEquals(brandEntity.getLibelle(),addBrandOutputDto.libelle());
    }

    @Test
    void shouldThrowExceptionWhenAlreadyBrandCreate(){
        BrandInputDto brandInputDto =new BrandInputDto("DC");
        when(brandRepository.existsByLibelle(brandInputDto.libelle())).thenReturn(true);

        BrandAlreadyExistsException brandAlreadyExistsException= Assertions.assertThrows(
                BrandAlreadyExistsException.class,
                () -> brandService.addBrand(brandInputDto)
        );
        Assertions.assertEquals("La Marque existe deja : brandName : DC", brandAlreadyExistsException.getMessage());
    }

    @Test
    void shouldUpdateBrand(){
        BrandInputDto brandInputDto =new BrandInputDto("DC");

        BrandEntity oldbrandEntity= BrandEntity.builder()
                .idBrand(1L)
                .libelle("Marvel")
                .build();
        BrandEntity brandEntity= BrandEntity.builder()
                .idBrand(1L)
                .libelle(brandInputDto.libelle())
                .build();
        when(brandRepository.findByIdBrand(1L)).thenReturn(Optional.ofNullable(oldbrandEntity));
        when(brandRepository.save(any(BrandEntity.class))).thenReturn(brandEntity);

        BrandOutputDto updateBrandOutputDto= brandService.updateBrand(1L,brandInputDto);

        Assertions.assertEquals("DC", updateBrandOutputDto.libelle());
    }

    @Test
    void shouldThrowExceptionWhenBrandNotExist(){
        BrandInputDto brandInputDto =new BrandInputDto("DC");
        when(brandRepository.findByIdBrand(1L)).thenReturn(Optional.empty());

        BrandNotFoundException brandNotFoundException= Assertions.assertThrows(
                BrandNotFoundException.class,
                () -> brandService.updateBrand(1L,brandInputDto)
        );
        Assertions.assertEquals("Aucune marque ne correspond à l'id de la marque : 1",brandNotFoundException.getMessage());
    }

    @Test
    void shouldShowAllBrand(){
        BrandEntity brandEntity=BrandEntity.builder().idBrand(1L).libelle("DC").build();
        BrandEntity brandEntity2=BrandEntity.builder().idBrand(2L).libelle("Marvel").build();
        List<BrandEntity> listBrandEntity=new ArrayList<>();
        listBrandEntity.add(brandEntity);
        listBrandEntity.add(brandEntity2);
        when(brandRepository.findAll()).thenReturn(listBrandEntity);

        List<BrandOutputDto> listBrandOutputDto= brandService.findAllBrands();

        Assertions.assertEquals(2, listBrandOutputDto.size());
        Assertions.assertEquals("DC", listBrandOutputDto.get(0).libelle());
    }

    @Test
    void shouldShowOnlyOneBrand(){
        BrandEntity brandEntity=BrandEntity.builder().idBrand(1L).libelle("DC").build();
        when(brandRepository.findByIdBrand(1L)).thenReturn(Optional.ofNullable(brandEntity));

        BrandOutputDto brandOutputDto= brandService.findBrandById(1L);

        Assertions.assertEquals("DC", brandOutputDto.libelle());
    }

    @Test
    void shouldThrowExceptionWhenNoBrandGetById(){
        when(brandRepository.findByIdBrand(1L)).thenReturn(Optional.empty());

        BrandNotFoundException brandNotFoundException= Assertions.assertThrows(
                BrandNotFoundException.class,
                () -> brandService.findBrandById(1L)
        );
        Assertions.assertEquals("Aucune marque ne correspond à l'id de la marque : 1",brandNotFoundException.getMessage());
    }

    @Test
    void shouldDeleteBrand(){
        BrandEntity brandEntity=BrandEntity.builder().idBrand(1L).libelle("DC").build();
        when(brandRepository.findByIdBrand(1L)).thenReturn(Optional.ofNullable(brandEntity));

        brandService.deleteBrand(1L);
    }

    @Test
    void shouldThrowExceptionWhenNoBrandDelete(){
        when(brandRepository.findByIdBrand(1L)).thenReturn(Optional.empty());

        BrandNotFoundException brandGetException= Assertions.assertThrows(
                BrandNotFoundException.class,
                () -> brandService.deleteBrand(1L)
        );
        Assertions.assertEquals("Aucune marque ne correspond à l'id de la marque : 1",brandGetException.getMessage());
    }



}