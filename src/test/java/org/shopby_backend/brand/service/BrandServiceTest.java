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
import org.shopby_backend.exception.brand.BrandCreateException;
import org.shopby_backend.exception.brand.BrandGetException;
import org.shopby_backend.exception.brand.BrandUpdateException;
import org.shopby_backend.users.model.UsersEntity;

import java.util.ArrayList;
import java.util.List;

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
        when(brandRepository.save(any(BrandEntity.class))).thenReturn(brandEntity);
        /// Act
        BrandOutputDto addBrandOutputDto= brandService.addBrand(brandInputDto);

        /// Asserts
        Assertions.assertEquals(brandEntity.getLibelle(),addBrandOutputDto.libelle());
    }

    @Test
    void shouldThrowExceptionWhenAlreadyBrandCreate(){
        BrandInputDto brandInputDto =new BrandInputDto("DC");
        BrandEntity alreadyBrand=BrandEntity.builder().idBrand(1L)
                        .libelle("DC").build();
        when(brandRepository.findByLibelle(brandInputDto.libelle())).thenReturn(alreadyBrand);

        BrandCreateException brandCreateException= Assertions.assertThrows(
                BrandCreateException.class,
                () -> brandService.addBrand(brandInputDto)
        );
        Assertions.assertEquals("Le libelle de votre marque existe deja", brandCreateException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNoBrandWriteCreate(){
        BrandInputDto brandInputDto =new BrandInputDto("");

        BrandCreateException brandCreateException= Assertions.assertThrows(
                BrandCreateException.class,
                () -> brandService.addBrand(brandInputDto)
        );
        Assertions.assertEquals("Le libelle de votre marque n'est pas valide", brandCreateException.getMessage());
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
        when(brandRepository.findByIdBrand(1L)).thenReturn(oldbrandEntity);
        when(brandRepository.save(any(BrandEntity.class))).thenReturn(brandEntity);

        BrandOutputDto updateBrandOutputDto= brandService.updateBrand(1L,brandInputDto);

        Assertions.assertEquals(updateBrandOutputDto.libelle(),"DC");
    }

    @Test
    void shouldThrowExceptionWhenNoBrandUpdate(){
        BrandInputDto brandInputDto =new BrandInputDto("");

        BrandUpdateException brandUpdateException= Assertions.assertThrows(
                BrandUpdateException.class,
                () -> brandService.updateBrand(1L,brandInputDto)
        );
        Assertions.assertEquals("Le libelle de votre marque n'existe pas",brandUpdateException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBrandNotExist(){
        BrandInputDto brandInputDto =new BrandInputDto("DC");
        when(brandRepository.findByIdBrand(1L)).thenReturn(null);

        BrandUpdateException brandUpdateException= Assertions.assertThrows(
                BrandUpdateException.class,
                () -> brandService.updateBrand(1L,brandInputDto)
        );
        Assertions.assertEquals("La marque saisie n'existe pas",brandUpdateException.getMessage());
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

        Assertions.assertEquals(listBrandOutputDto.size(),2);
        Assertions.assertEquals(listBrandOutputDto.get(0).libelle(),"DC");
    }

    @Test
    void shouldShowOnlyOneBrand(){
        BrandEntity brandEntity=BrandEntity.builder().idBrand(1L).libelle("DC").build();
        when(brandRepository.findByIdBrand(1L)).thenReturn(brandEntity);

        BrandOutputDto brandOutputDto= brandService.findBrandById(1L);

        Assertions.assertEquals(brandOutputDto.libelle(),"DC");
    }

    @Test
    void shouldThrowExceptionWhenNoBrandGetById(){
        when(brandRepository.findByIdBrand(1L)).thenReturn(null);

        BrandGetException brandGetException= Assertions.assertThrows(
                BrandGetException.class,
                () -> brandService.findBrandById(1L)
        );
        Assertions.assertEquals("L'id saisie ne correspond à aucune marque",brandGetException.getMessage());
    }



}