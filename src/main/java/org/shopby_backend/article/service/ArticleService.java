package org.shopby_backend.article.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.exception.article.ArticleDeleteException;
import org.shopby_backend.exception.article.ArticleGetException;
import org.shopby_backend.exception.article.ArticleUpdateException;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;
import org.shopby_backend.exception.article.ArticleCreateException;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

        if(addArticleInputDto.nameArticle()==null||addArticleInputDto.nameArticle().isBlank()){
            throw new ArticleCreateException("L'article doit avoir un nom défini");
        }

        if(addArticleInputDto.descriptionArticle()==null|| addArticleInputDto.descriptionArticle().isBlank()){
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
        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(addArticleInputDto.idType()).orElseThrow(()->new ArticleCreateException("Le Type de article est introuvable"));

        ArticleEntity article = ArticleEntity.builder()
                .name(addArticleInputDto.nameArticle())
                .description(addArticleInputDto.descriptionArticle())
                .brand(brandEntity)
                .typeArticle(typeArticleEntity)
                .price(addArticleInputDto.price())
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

    public AddArticleOutputDto updateArticle(Long id,AddArticleInputDto addArticleInputDto){
        ArticleEntity articleEntity = articleRepository.findById(id).orElseThrow(()-> new ArticleUpdateException("Aucun article ne corresond à votre id d'article"));

        if(addArticleInputDto.nameArticle()!=null){
            articleEntity.setName(addArticleInputDto.nameArticle());
        }
        if(addArticleInputDto.descriptionArticle()!=null){
            articleEntity.setDescription(addArticleInputDto.descriptionArticle());
        }
        if(addArticleInputDto.price()!=null){
            articleEntity.setPrice(addArticleInputDto.price());
        }
        if(addArticleInputDto.idBrand()!=null){
            if(!Objects.equals(articleEntity.getBrand().getIdBrand(), addArticleInputDto.idBrand())){
                BrandEntity brandEntity= brandRepository.findByIdBrand(addArticleInputDto.idBrand());
                if(brandEntity!=null){
                    articleEntity.setBrand(brandEntity);
                }
                else{
                    throw new ArticleUpdateException("Aucune Marque ne correspond à l'id de marque de brand saisie");
                }

            }
        }
        if(addArticleInputDto.idType()!=null){
            if(!Objects.equals(articleEntity.getTypeArticle().getIdTypeArticle(), addArticleInputDto.idType())){
                TypeArticleEntity type=typeArticleRepository.findById(addArticleInputDto.idType()).orElseThrow(()->new ArticleUpdateException("Aucun type ne correspond à l'id de type saisie"));
                if(type!=null){
                    articleEntity.setTypeArticle(type);
                }
            }
        }
        ArticleEntity updatedArticle=articleRepository.save(articleEntity);
        return new AddArticleOutputDto(updatedArticle.getIdArticle(),updatedArticle.getName(),updatedArticle.getDescription(),updatedArticle.getPrice(),updatedArticle.getBrand().getLibelle(),updatedArticle.getTypeArticle().getLibelle(),updatedArticle.getCreationDate());
    }

    public AddArticleOutputDto deleteArticle(Long id){
        ArticleEntity articleEntity=articleRepository.findById(id).orElseThrow(()-> new ArticleDeleteException("Aucun article ne correspond à l'id de l'article"));
        articleRepository.delete(articleEntity);
        return new AddArticleOutputDto(articleEntity.getIdArticle(),articleEntity.getName(),articleEntity.getDescription(),articleEntity.getPrice(),articleEntity.getBrand().getLibelle(),articleEntity.getTypeArticle().getLibelle(),articleEntity.getCreationDate());
    }

    public List<AddArticleOutputDto> getAllArticles(){
       return articleRepository.findAll().stream().map(article->{
           return new AddArticleOutputDto(article.getIdArticle(),article.getName(),article.getDescription(),article.getPrice(),article.getBrand().getLibelle(),article.getTypeArticle().getLibelle(),article.getCreationDate());
        }).toList();
    }

    public AddArticleOutputDto getArticleById(Long id){
        ArticleEntity articleEntity=articleRepository.findById(id).orElseThrow(()->new ArticleGetException("Aucun article ne correspond à l'id de l'article"));
        return new AddArticleOutputDto(articleEntity.getIdArticle(),articleEntity.getName(),articleEntity.getDescription(),articleEntity.getPrice(),articleEntity.getBrand().getLibelle(),articleEntity.getTypeArticle().getLibelle(),articleEntity.getCreationDate());
    }
}
