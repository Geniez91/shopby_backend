package org.shopby_backend.article.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.dto.ArticleFilter;
import org.shopby_backend.article.mapper.ArticleMapper;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.brand.dto.BrandFilter;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.shopby_backend.brand.service.BrandService;
import org.shopby_backend.comment.persistence.CommentRepository;
import org.shopby_backend.exception.article.*;
import org.shopby_backend.exception.brand.BrandNotFoundException;
import org.shopby_backend.exception.typeArticle.TypeArticleNotFoundException;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;
import org.shopby_backend.typeArticle.service.TypeArticleService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    ArticleRepository articleRepository;

    @Mock
    BrandService brandService;

    @Mock
    TypeArticleService typeArticleService;

    @Mock
    CommentRepository commentRepository;

    ArticleService articleService;

    ArticleMapper articleMapper=new ArticleMapper();

    @BeforeEach
    void setUp() {
        articleService=new ArticleService(articleRepository,articleMapper,brandService,typeArticleService,commentRepository);
    }

    @Test
    void shouldAddArticle(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("name","description",BigDecimal.valueOf(2),1L,1L);
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(brandService.findBrandOrThrow(addArticleInputDto.idBrand())).thenReturn(brandEntity);
        when(typeArticleService.findTypeArticleOrThrow(addArticleInputDto.idType())).thenReturn(typeArticleEntity);
        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(articleEntity);

        AddArticleOutputDto addArticleOutputDto = articleService.addNewArticle(addArticleInputDto);

        Assertions.assertEquals(addArticleInputDto.nameArticle(),addArticleOutputDto.nomArticle());
    }

    @Test
    void shouldThrownAnExceptionAddWhenArticleAlreadyExists(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("name","description",BigDecimal.valueOf(2),1L,1L);
        when(articleRepository.existsByName(addArticleInputDto.nameArticle())).thenReturn(true);

        ArticleAlreadyExistsException articleAlreadyExistsException=Assertions.assertThrows(ArticleAlreadyExistsException.class,()->{
            articleService.addNewArticle(addArticleInputDto);
        });
        Assertions.assertEquals("L'article existe deja : articleName :name",articleAlreadyExistsException.getMessage());
    }


    @Test
    void shouldThrownAnExceptionAddWhenBrandEmpty(){
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("Superman","description",BigDecimal.valueOf(2),1L,1L);
        when(brandService.findBrandOrThrow(addArticleInputDto.idBrand())).thenThrow(new BrandNotFoundException(1L));

        BrandNotFoundException brandNotFoundException=Assertions.assertThrows(BrandNotFoundException.class,()->{
            articleService.addNewArticle(addArticleInputDto);
        });
        Assertions.assertEquals("Aucune marque ne correspond à l'id de la marque : 1",brandNotFoundException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionAddWhenTypeEmpty(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("Superman","description",BigDecimal.valueOf(2),1L,1L);
        when(brandService.findBrandOrThrow(addArticleInputDto.idBrand())).thenReturn(brandEntity);
        when(typeArticleService.findTypeArticleOrThrow(addArticleInputDto.idType())).thenThrow(new TypeArticleNotFoundException("Aucun type article n'existe avec l'id 1"));

        TypeArticleNotFoundException typeArticleNotFoundException=Assertions.assertThrows(TypeArticleNotFoundException.class,()->{
            articleService.addNewArticle(addArticleInputDto);
        });
        Assertions.assertEquals("Aucun type article n'existe avec l'id 1",typeArticleNotFoundException.getMessage());
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

        ArticleNotFoundException articleNotFoundException=Assertions.assertThrows(ArticleNotFoundException.class,()->{
            articleService.updateArticle(1L,addArticleInputDto);
        });

        Assertions.assertEquals("Aucun article ne correspond à l'id de l'article 1",articleNotFoundException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionUpdateWhenBrandNotCorrect(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("test",null,null,2L,null);
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(articleEntity));
        when(brandService.findBrandOrThrow(addArticleInputDto.idBrand())).thenThrow(new BrandNotFoundException(2L));

        BrandNotFoundException brandNotFoundException=Assertions.assertThrows(BrandNotFoundException.class,()->{
            articleService.updateArticle(1L,addArticleInputDto);
        });

        Assertions.assertEquals("Aucune marque ne correspond à l'id de la marque : 2",brandNotFoundException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionUpdateWhenTypeNotCorrect(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        AddArticleInputDto addArticleInputDto=new AddArticleInputDto("test",null,null,null,2L);
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(articleEntity));
        when(typeArticleService.findTypeArticleOrThrow(addArticleInputDto.idType())).thenThrow(new TypeArticleNotFoundException("Aucun type article n'existe avec l'id 2"));

        TypeArticleNotFoundException typeArticleNotFoundException=Assertions.assertThrows(TypeArticleNotFoundException.class,()->{
            articleService.updateArticle(1L,addArticleInputDto);
        });

        Assertions.assertEquals("Aucun type article n'existe avec l'id 2",typeArticleNotFoundException.getMessage());
    }


    @Test
    void shouldDeleteArticle(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        when(articleRepository.findById(1L)).thenReturn(Optional.of(articleEntity));

        articleService.deleteArticle(1L);

        verify(articleRepository).delete(articleEntity);

    }



    @Test
    void shouldThrownAnExceptionDeleteWhenNoArticleFound(){
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        ArticleNotFoundException articleNotFoundException=Assertions.assertThrows(ArticleNotFoundException.class,()->{
            articleService.deleteArticle(1L);
        });
        Assertions.assertEquals("Aucun article ne correspond à l'id de l'article 1",articleNotFoundException.getMessage());
    }

    @Test
    void shouldGetAllArticles(){
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        ArticleEntity articleEntity2=ArticleEntity.builder().idArticle(2L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        List<ArticleEntity> articleEntityList= List.of(articleEntity,articleEntity2);
        Page<ArticleEntity> page = new PageImpl<>(articleEntityList);
        ArticleFilter articleFilter=new ArticleFilter(BigDecimal.valueOf(50),BigDecimal.valueOf(10),1L,1L,1L,"name");
        Pageable pageable = PageRequest.of(0, 10);
        when(articleRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(page);

        Page<AddArticleOutputDto> listArticleEntity=articleService.getFilteredArticles(articleFilter,pageable);
        Assertions.assertEquals(2,listArticleEntity.getContent().size());
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

        ArticleNotFoundException articleNotFoundException=Assertions.assertThrows(ArticleNotFoundException.class,()->{
            articleService.getArticleById(1L);
        });
        Assertions.assertEquals("Aucun article ne correspond à l'id de l'article 1",articleNotFoundException.getMessage());
    }






}