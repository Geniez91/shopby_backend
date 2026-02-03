package org.shopby_backend.article.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.exception.article.ArticleDeleteException;
import org.shopby_backend.exception.article.ArticleGetException;
import org.shopby_backend.exception.article.ArticleUpdateException;
import org.shopby_backend.order.service.OrderService;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;
import org.shopby_backend.exception.article.ArticleCreateException;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class ArticleService {
    private ArticleRepository articleRepository;
    private BrandRepository brandRepository;
    private TypeArticleRepository typeArticleRepository;
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    public AddArticleOutputDto addNewArticle(AddArticleInputDto addArticleInputDto){
        long start = System.nanoTime();
        if(articleRepository.findByName(addArticleInputDto.nameArticle())!=null){
            String message = "L'article existe deja";
            ArticleCreateException exception = new ArticleCreateException(message);
            logger.error(message,exception);
            throw exception;
        }

        if(addArticleInputDto.nameArticle()==null||addArticleInputDto.nameArticle().isBlank()){
            String message = "L'article doit avoir un nom défini";
            ArticleCreateException exception = new ArticleCreateException(message);
            logger.error(message,exception);
            throw exception;
        }

        if(addArticleInputDto.descriptionArticle()==null|| addArticleInputDto.descriptionArticle().isBlank()){
            String message = "L'article doit avoir une description défini";
            ArticleCreateException exception = new ArticleCreateException(message);
            logger.error(message,exception);
            throw exception;
        }

        if(addArticleInputDto.price()==null){
            String message = "L'article doit avoir une valeur valide";
            ArticleCreateException exception = new ArticleCreateException(message);
            logger.error(message,exception);
            throw exception;
        }

        if(addArticleInputDto.idBrand()==null){
            String message = "L'article doit avoir un identifiant de marque défini";
            ArticleCreateException exception = new ArticleCreateException(message);
            logger.error(message,exception);
            throw exception;
        }

        if(addArticleInputDto.idType()==null){
            String message = "L'article doit avoir un identifiant de type d'article";
            ArticleCreateException exception = new ArticleCreateException(message);
            logger.error(message,exception);
            throw exception;
        }

        BrandEntity brandEntity = brandRepository.findById(addArticleInputDto.idBrand()).orElseThrow(()->
        {
            ArticleCreateException exception = new ArticleCreateException("La Marque est introuvable " +addArticleInputDto.idBrand());
            logger.error("La Marque est introuvable {}",addArticleInputDto.idBrand(),exception );
            return exception;
        });

        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(addArticleInputDto.idType()).orElseThrow(()->
        {
            ArticleCreateException exception = new ArticleCreateException("Le Type d'article est introuvable "+addArticleInputDto.idType());
            logger.error("Le Type d'article est introuvable {}",addArticleInputDto.idType(),exception );
            return exception;
        });

        ArticleEntity article = ArticleEntity.builder()
                .name(addArticleInputDto.nameArticle())
                .description(addArticleInputDto.descriptionArticle())
                .brand(brandEntity)
                .typeArticle(typeArticleEntity)
                .price(addArticleInputDto.price())
                .build();

        ArticleEntity savedArticle = articleRepository.save(article);
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        logger.info("L'article {} a bien été ajouté avec le nom {}, la description {}, le prix {}, la marque {}, le type d'article {}, durationMs : {}",savedArticle.getIdArticle(),savedArticle.getName(),savedArticle.getDescription(),savedArticle.getPrice(), brandEntity.getLibelle(),typeArticleEntity.getLibelle(),durationMs);

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
        long start = System.nanoTime();
        ArticleEntity articleEntity = articleRepository.findById(id).orElseThrow(()->{
            ArticleUpdateException exception = new ArticleUpdateException("Aucun article ne corresond à votre id d'article "+id);
            logger.error("Aucun article ne corresond à votre id d'article {}",id,exception );
            return exception;
        });

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
                BrandEntity brandEntity= brandRepository.findByIdBrand(addArticleInputDto.idBrand()).orElseThrow(()->{
                    ArticleUpdateException exception = new ArticleUpdateException("Aucune Marque ne correspond à l'id de marque de brand saisie");
                    logger.error("Aucune Marque ne correspond à l'id de marque de brand saisie {}", addArticleInputDto.idBrand(),exception);
                    return exception;
                });
                articleEntity.setBrand(brandEntity);
            }
        }
        if(addArticleInputDto.idType()!=null){
            if(!Objects.equals(articleEntity.getTypeArticle().getIdTypeArticle(), addArticleInputDto.idType())){
                TypeArticleEntity type=typeArticleRepository.findById(addArticleInputDto.idType()).orElseThrow(()->
                {
                    ArticleUpdateException exception = new ArticleUpdateException("Aucun type ne correspond à l'id de type saisie "+addArticleInputDto.idType());
                    logger.error("Aucun type ne correspond à l'id de type saisie {}",addArticleInputDto.idType(),exception);
                    return exception;
                });

                if(type!=null){
                    articleEntity.setTypeArticle(type);
                }
            }
        }
        ArticleEntity updatedArticle=articleRepository.save(articleEntity);
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        logger.info("L'article {} a bien été mise à jour,durationMs={}",updatedArticle.getIdArticle(),durationMs);
        return new AddArticleOutputDto(updatedArticle.getIdArticle(),updatedArticle.getName(),updatedArticle.getDescription(),updatedArticle.getPrice(),updatedArticle.getBrand().getLibelle(),updatedArticle.getTypeArticle().getLibelle(),updatedArticle.getCreationDate());
    }

    public AddArticleOutputDto deleteArticle(Long id){
        long start = System.nanoTime();
        ArticleEntity articleEntity=articleRepository.findById(id).orElseThrow(()->
        {
            ArticleDeleteException exception = new ArticleDeleteException("Aucun article ne correspond à l'id de l'article "+id);
            logger.error("Aucun article ne correspond à l'id de l'article {}",id,exception);
            return exception;
        });
        articleRepository.delete(articleEntity);
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        logger.info("L'article {} a bien été supprimé, durationMs = {}",articleEntity.getIdArticle(),durationMs);
        return new AddArticleOutputDto(articleEntity.getIdArticle(),articleEntity.getName(),articleEntity.getDescription(),articleEntity.getPrice(),articleEntity.getBrand().getLibelle(),articleEntity.getTypeArticle().getLibelle(),articleEntity.getCreationDate());
    }

    public List<AddArticleOutputDto> getAllArticles(){
        long start = System.nanoTime();
        List<AddArticleOutputDto> listArticle = articleRepository.findAll().stream().map(article->{
           return new AddArticleOutputDto(article.getIdArticle(),article.getName(),article.getDescription(),article.getPrice(),article.getBrand().getLibelle(),article.getTypeArticle().getLibelle(),article.getCreationDate());
        }).toList();
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        logger.info("{} articles ont été trouvés, durationMs = {}",listArticle.size(),durationMs);
        return listArticle;
    }

    public AddArticleOutputDto getArticleById(Long id){
        long start = System.nanoTime();
        ArticleEntity articleEntity=articleRepository.findById(id).orElseThrow(()->{
            ArticleGetException exception = new ArticleGetException("Aucun article ne correspond à l'id de l'article "+id);
            logger.error("Aucun article ne correspond à l'id de l'article {}",id,exception);
            return exception;
        });
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        logger.info("L'article {} a bien été trouvé, durationMs = {}",articleEntity.getIdArticle(),durationMs);
        return new AddArticleOutputDto(articleEntity.getIdArticle(),articleEntity.getName(),articleEntity.getDescription(),articleEntity.getPrice(),articleEntity.getBrand().getLibelle(),articleEntity.getTypeArticle().getLibelle(),articleEntity.getCreationDate());
    }
}
