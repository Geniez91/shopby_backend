package org.shopby_backend.article.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.article.model.TypeArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.article.persistence.TypeArticleRepository;
import org.shopby_backend.exception.article.ArticleCreateException;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class ArticleService {
    private ArticleRepository articleRepository;
    private BrandRepository brandRepository;
    private TypeArticleRepository typeArticleRepository;

    public AddArticleOutputDto addNewArticle(AddArticleInputDto addArticleInputDto){
        if(articleRepository.findByName(addArticleInputDto.nameArticle())!=null){
            throw new ArticleCreateException("L'article existe deja");
        }

        if(addArticleInputDto.nameArticle().isBlank()){
            throw new ArticleCreateException("L'article doit avoir un nom défini");
        }

        if(addArticleInputDto.descriptionArticle().isBlank()){
            throw new ArticleCreateException("L'article doit avoir une description défini");
        }

        if(addArticleInputDto.price()==null){
            throw new ArticleCreateException("L'article doit avoir une valeur valide");
        }

        if(addArticleInputDto.idBrand()==null){
            throw new ArticleCreateException("L'article doit avoir un identifiant de marque défini");
        }

        if(addArticleInputDto.idType()==null){
            throw new ArticleCreateException("L'article doit avoir un identifiant de type d'article");
        }

        BrandEntity brandEntity = brandRepository.findById(addArticleInputDto.idBrand()).orElseThrow(()->new ArticleCreateException("La Marque est introuvable"));
        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(addArticleInputDto.idType()).orElseThrow(()->new ArticleCreateException("La Type de article est introuvable"));

        ArticleEntity article = ArticleEntity.builder()
                .name(addArticleInputDto.nameArticle())
                .description(addArticleInputDto.descriptionArticle())
                .brand(brandEntity)
                .typeArticle(typeArticleEntity)
                .price((BigDecimal) addArticleInputDto.price())
                .build();
        ArticleEntity savedArticle=articleRepository.save(article);

        return new AddArticleOutputDto(
                savedArticle.getIdArticle(),
                savedArticle.getName(),
                savedArticle.getDescription(),
                savedArticle.getPrice(),
                brandEntity.getLibelle(),
                typeArticleEntity.getLibelle(),
                savedArticle.getCreationDate()
        );

    }
}
