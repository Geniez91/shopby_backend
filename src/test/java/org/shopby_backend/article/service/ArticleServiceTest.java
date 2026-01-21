package org.shopby_backend.article.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.shopby_backend.brand.service.BrandService;
import org.shopby_backend.exception.article.ArticleCreateException;
import org.shopby_backend.exception.article.ArticleDeleteException;
import org.shopby_backend.exception.article.ArticleGetException;
import org.shopby_backend.exception.article.ArticleUpdateException;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    ArticleRepository articleRepository;

    @Mock
    BrandRepository brandRepository;

    @Mock
    TypeArticleRepository typeArticleRepository;

    @InjectMocks
    ArticleService articleService;

    @Test
    void shouldAddArticle(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("name","description",BigDecimal.valueOf(2),1L,1L);
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(brandRepository.findById(addArticleInputDto.idBrand())).thenReturn(Optional.of(brandEntity));
        when(typeArticleRepository.findById(addArticleInputDto.idType())).thenReturn(Optional.of(typeArticleEntity));
        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(articleEntity);

        AddArticleOutputDto addArticleOutputDto = articleService.addNewArticle(addArticleInputDto);

        Assertions.assertEquals(addArticleInputDto.nameArticle(),addArticleOutputDto.nomArticle());
    }

    @Test
    void shouldThrownAnExceptionAddWhenArticleAlreadyExists(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("name","description",BigDecimal.valueOf(2),1L,1L);
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(articleRepository.findByName(addArticleInputDto.nameArticle())).thenReturn(articleEntity);

        ArticleCreateException articleCreateException=Assertions.assertThrows(ArticleCreateException.class,()->{
            articleService.addNewArticle(addArticleInputDto);
        });
        Assertions.assertEquals("L'article existe deja",articleCreateException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionWhenArticleNameEmpty(){
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto(null,"description",BigDecimal.valueOf(2),1L,1L);

        ArticleCreateException articleCreateException=Assertions.assertThrows(ArticleCreateException.class,()->{
            articleService.addNewArticle(addArticleInputDto);
        });
        Assertions.assertEquals("L'article doit avoir un nom défini",articleCreateException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionAddWhenBrandEmpty(){
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("Superman","description",BigDecimal.valueOf(2),1L,1L);
        when(brandRepository.findById(addArticleInputDto.idBrand())).thenReturn(Optional.empty());

        ArticleCreateException articleCreateException=Assertions.assertThrows(ArticleCreateException.class,()->{
            articleService.addNewArticle(addArticleInputDto);
        });
        Assertions.assertEquals("La Marque est introuvable",articleCreateException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionAddWhenTypeEmpty(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("Superman","description",BigDecimal.valueOf(2),1L,1L);
        when(brandRepository.findById(addArticleInputDto.idBrand())).thenReturn(Optional.ofNullable(brandEntity));
        when(typeArticleRepository.findById(addArticleInputDto.idType())).thenReturn(Optional.empty());

        ArticleCreateException articleCreateException=Assertions.assertThrows(ArticleCreateException.class,()->{
            articleService.addNewArticle(addArticleInputDto);
        });
        Assertions.assertEquals("Le Type de article est introuvable",articleCreateException.getMessage());
    }

    @Test
    void shouldUpdateArticle(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("test",null,null,null,null);
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        ArticleEntity articleUpdated=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("test").build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(articleEntity));
        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(articleUpdated);

        AddArticleOutputDto addArticleOutputDto=articleService.updateArticle(1L,addArticleInputDto);

        Assertions.assertEquals(addArticleInputDto.nameArticle(),addArticleOutputDto.nomArticle());
    }

    @Test
    void shouldThrownAnExceptionUpdateWhenNoArticleFound(){
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("test",null,null,null,null);
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        ArticleUpdateException articleUpdateException=Assertions.assertThrows(ArticleUpdateException.class,()->{
            articleService.updateArticle(1L,addArticleInputDto);
        });

        Assertions.assertEquals("Aucun article ne corresond à votre id d'article",articleUpdateException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionUpdateWhenBrandNotCorrect(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("test",null,null,2L,null);
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(articleEntity));
        when(brandRepository.findByIdBrand(addArticleInputDto.idBrand())).thenReturn(null);

        ArticleUpdateException articleUpdateException=Assertions.assertThrows(ArticleUpdateException.class,()->{
            articleService.updateArticle(1L,addArticleInputDto);
        });

        Assertions.assertEquals("Aucune Marque ne correspond à l'id de marque de brand saisie",articleUpdateException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionUpdateWhenTypeNotCorrect(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("test",null,null,null,2L);
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(articleEntity));
        when(typeArticleRepository.findById(addArticleInputDto.idType())).thenReturn(Optional.empty());

        ArticleUpdateException articleUpdateException=Assertions.assertThrows(ArticleUpdateException.class,()->{
            articleService.updateArticle(1L,addArticleInputDto);
        });

        Assertions.assertEquals("Aucun type ne correspond à l'id de type saisie",articleUpdateException.getMessage());
    }

    @Test
    void shouldDeleteArticle(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(articleEntity));

        AddArticleOutputDto addArticleOutputDto=articleService.deleteArticle(1L);
        Assertions.assertEquals("name",addArticleOutputDto.nomArticle());

    }

    @Test
    void shouldThrownAnExceptionDeleteWhenNoArticleFound(){
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        ArticleDeleteException articleDeleteException=Assertions.assertThrows(ArticleDeleteException.class,()->{
            articleService.deleteArticle(1L);
        });
        Assertions.assertEquals("Aucun article ne correspond à l'id de l'article",articleDeleteException.getMessage());
    }

    @Test
    void shouldGetAllArticles(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        ArticleEntity articleEntity2=ArticleEntity.builder().idArticle(2L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        List<ArticleEntity> articleEntityList= List.of(articleEntity,articleEntity2);
        when(articleRepository.findAll()).thenReturn(articleEntityList);

        List<AddArticleOutputDto> listArticleEntiy=articleService.getAllArticles();
        Assertions.assertEquals(2,listArticleEntiy.size());
    }

    @Test
    void shouldGetArticleById(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(articleEntity));

        AddArticleOutputDto addArticleOutputDto=articleService.getArticleById(1L);

        Assertions.assertEquals("name",addArticleOutputDto.nomArticle());
    }

    @Test
    void shouldThrowAnExceptionGetArticleByIdWhenNoArticleFound(){
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        ArticleGetException articleGetException=Assertions.assertThrows(ArticleGetException.class,()->{
            articleService.getArticleById(1L);
        });
        Assertions.assertEquals("Aucun article ne correspond à l'id de l'article",articleGetException.getMessage());
    }






}