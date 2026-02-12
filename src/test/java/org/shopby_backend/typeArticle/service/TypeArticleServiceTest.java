package org.shopby_backend.typeArticle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.shopby_backend.brand.service.BrandService;
import org.shopby_backend.exception.brand.BrandCreateException;
import org.shopby_backend.exception.typeArticle.*;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TypeArticleServiceTest {
    @Mock
    TypeArticleRepository typeArticleRepository;

    @InjectMocks
    TypeArticleService typeArticleService;

    @Test
    void shouldAddTypeArticleWithoutParent(){
        TypeArticleDto typeArticleDto = new TypeArticleDto("DC",null);
        TypeArticleEntity typeArticleEntity = TypeArticleEntity.builder()
                .idTypeArticle(1L)
                .libelle("DC")
                .idTypeArticle(null)
                .build();
        when(typeArticleRepository.existsByLibelle("DC")).thenReturn(false);
        when(typeArticleRepository.save(any(TypeArticleEntity.class))).thenReturn(typeArticleEntity);

        TypeArticleOutputDto typeArticleOutputDto=typeArticleService.addTypeArticle(typeArticleDto);

        Assertions.assertEquals(typeArticleDto.libelle(),typeArticleOutputDto.libelle());
    }

    @Test
    void shoudAddTypeArticleWithParent(){
        TypeArticleDto typeArticleDto = new TypeArticleDto("DC",2L);
        TypeArticleEntity parent=TypeArticleEntity.builder().idTypeArticle(2L).libelle("Comics").parent(null).build();
        TypeArticleEntity typeArticleEntity = TypeArticleEntity.builder().libelle("DC").parent(parent).build();
        when(typeArticleRepository.existsByLibelle("DC")).thenReturn(false);
        when(typeArticleRepository.findById(any())).thenReturn(Optional.ofNullable(parent));
        when(typeArticleRepository.save(any(TypeArticleEntity.class))).thenReturn(typeArticleEntity);

        TypeArticleOutputDto typeArticleOutputDto=typeArticleService.addTypeArticle(typeArticleDto);

        Assertions.assertEquals(typeArticleDto.libelle(),typeArticleOutputDto.libelle());
        Assertions.assertEquals(typeArticleDto.parentId(),typeArticleOutputDto.parentId());
    }

    @Test
    void shouldThrowAnExceptionWhenTypeArticleAlreadyExist(){
        TypeArticleDto typeArticleDto = new TypeArticleDto("DC",null);
        when(typeArticleRepository.existsByLibelle("DC")).thenReturn(true);

        TypeArticleAlreadyExistsException typeArticleAlreadyExistsException= Assertions.assertThrows(
                TypeArticleAlreadyExistsException.class,
                () -> typeArticleService.addTypeArticle(typeArticleDto)
        );
        Assertions.assertEquals("Le type d'article existe deja avec le libelle DC",typeArticleAlreadyExistsException.getMessage());
    }

    @Test
    void shouldUpdateTypeArticle(){
        TypeArticleDto typeArticleDto = new TypeArticleDto("DC",null);
        TypeArticleEntity parent=TypeArticleEntity.builder().idTypeArticle(2L).libelle("Comics").parent(null).build();
        TypeArticleEntity oldtypeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("DC").parent(parent).build();
        TypeArticleEntity newTypeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle(typeArticleDto.libelle()).parent(parent).build();

        when(typeArticleRepository.findById(1L)).thenReturn(Optional.of(oldtypeArticleEntity));
        when(typeArticleRepository.save(any(TypeArticleEntity.class))).thenReturn(newTypeArticleEntity);

        TypeArticleOutputDto typeArticleOutputDto=typeArticleService.updateTypeArticle(1L,typeArticleDto);

        Assertions.assertEquals(typeArticleDto.libelle(),typeArticleOutputDto.libelle());
    }

    @Test
    void shouldThrownAnExceptionWhenUpdateTypeArticleWithoutLibelle(){
        TypeArticleDto typeArticleDto = new TypeArticleDto(null,null);

        TypeArticleNotFoundException typeArticleNotFoundException= Assertions.assertThrows(
                TypeArticleNotFoundException.class,
                () -> typeArticleService.updateTypeArticle(1L,typeArticleDto)
        );
        Assertions.assertEquals("Aucun type article n'existe avec l'id 1",typeArticleNotFoundException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionWhenUpdateTypeArticleWhenNoFound(){
        TypeArticleDto typeArticleDto = new TypeArticleDto("test",null);
        when(typeArticleRepository.findById(1L)).thenReturn(Optional.empty());
        TypeArticleNotFoundException typeArticleNotFoundException= Assertions.assertThrows(
                TypeArticleNotFoundException.class,
                () -> typeArticleService.updateTypeArticle(1L,typeArticleDto)
        );
        Assertions.assertEquals("Aucun type article n'existe avec l'id 1",typeArticleNotFoundException.getMessage());
    }


    @Test
    void shouldDeleteTypeArticle(){
        TypeArticleEntity parent=TypeArticleEntity.builder().idTypeArticle(2L).libelle("Comics").parent(null).build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("test").parent(parent).build();
        when(typeArticleRepository.findById(1L)).thenReturn(Optional.ofNullable(typeArticleEntity));

        typeArticleService.deleteTypeArticle(1L);

        verify(typeArticleRepository).delete(typeArticleEntity);
    }



    @Test
    void shouldThrowAnExceptionWhenDeleteTypeArticleNotFoundType(){
        when(typeArticleRepository.findById(1L)).thenReturn(Optional.empty());

        TypeArticleNotFoundException typeArticleNotFoundException=Assertions.assertThrows(
                TypeArticleNotFoundException.class,
                () -> typeArticleService.deleteTypeArticle(1L)
        );
        Assertions.assertEquals("Aucun type article n'existe avec l'id 1",typeArticleNotFoundException.getMessage());
    }

    @Test
    void shouldGetAllTypeArticles(){
        TypeArticleEntity parent=TypeArticleEntity.builder().idTypeArticle(2L).libelle("Comics").parent(null).build();
        TypeArticleEntity parent2=TypeArticleEntity.builder().idTypeArticle(2L).libelle("Comics").parent(null).build();
        TypeArticleEntity type1=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").parent(parent).build();
        TypeArticleEntity type2=TypeArticleEntity.builder().idTypeArticle(2L).libelle("DC").parent(parent2).build();
        List<TypeArticleEntity> typeArticleEntityList= new ArrayList<>();
        typeArticleEntityList.add(type1);
        typeArticleEntityList.add(type2);
        when(typeArticleRepository.findAll()).thenReturn(typeArticleEntityList);

        List<TypeArticleOutputDto> listTypeArticleOutputDto=typeArticleService.getAllTypeArticle();

        Assertions.assertEquals(2,listTypeArticleOutputDto.size());
    }

    @Test
    void shouldGetTypeArticleById(){
        TypeArticleEntity parent=TypeArticleEntity.builder().idTypeArticle(2L).libelle("Comics").parent(null).build();
        TypeArticleEntity type1=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").parent(parent).build();
        when(typeArticleRepository.findById(1L)).thenReturn(Optional.of(type1));

        TypeArticleOutputDto typeArticleOutputDto=typeArticleService.getTypeArticleById(1L);
        Assertions.assertEquals(1L,typeArticleOutputDto.id());
    }

    @Test
    void shouldThrowAnExceptionWhenGetTypeArticleByIdNotFoundType(){
        when(typeArticleRepository.findById(1L)).thenReturn(Optional.empty());

        TypeArticleNotFoundException typeArticleNotFoundException= Assertions.assertThrows(
                TypeArticleNotFoundException.class,
                () -> typeArticleService.getTypeArticleById(1L));
        Assertions.assertEquals("Aucun type article n'existe avec l'id 1",typeArticleNotFoundException.getMessage());
    }




}