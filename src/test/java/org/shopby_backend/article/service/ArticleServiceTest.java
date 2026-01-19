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
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;

import java.math.BigDecimal;
import java.util.Date;
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
}