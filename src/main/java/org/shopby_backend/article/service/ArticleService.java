package org.shopby_backend.article.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.mapper.ArticleMapper;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.exception.article.*;
import org.shopby_backend.exception.brand.BrandNotFoundException;
import org.shopby_backend.exception.typeArticle.TypeArticleNotFoundException;
import org.shopby_backend.order.service.OrderService;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;
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
    private ArticleMapper articleMapper;

    public AddArticleOutputDto addNewArticle(AddArticleInputDto addArticleInputDto){
        long start = System.nanoTime();
        if(articleRepository.existsByName(addArticleInputDto.nameArticle())){
            ArticleAlreadyExistsException exception = new ArticleAlreadyExistsException(addArticleInputDto.nameArticle());
            log.warn(LogMessages.ARTICLE_ALREADY_EXISTS,addArticleInputDto.nameArticle());
            throw exception;
        }

        BrandEntity brandEntity = brandRepository.findById(addArticleInputDto.idBrand()).orElseThrow(()->
        {
            BrandNotFoundException exception = new BrandNotFoundException(addArticleInputDto.idBrand());
            log.warn(LogMessages.BRAND_NOT_FOUND,addArticleInputDto.idBrand());
            return exception;
        });

        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(addArticleInputDto.idType()).orElseThrow(()->
        {
            TypeArticleNotFoundException exception = TypeArticleNotFoundException.byId(addArticleInputDto.idType());
            log.warn(LogMessages.TYPE_ARTICLE_NOT_FOUND_BY_ID,addArticleInputDto.idType(),exception );
            return exception;
        });

        ArticleEntity article = articleMapper.toEntity(addArticleInputDto,typeArticleEntity,brandEntity);

        ArticleEntity savedArticle = articleRepository.save(article);
        long durationMs = Tools.getDurationMs(start);
        log.info("L'article {} a bien été ajouté avec le nom {}, la description {}, le prix {}, la marque {}, le type d'article {}, durationMs : {}",
                savedArticle.getIdArticle(),
                savedArticle.getName(),
                savedArticle.getDescription(),
                savedArticle.getPrice(),
                brandEntity.getLibelle(),
                typeArticleEntity.getLibelle(),
                durationMs);

        return articleMapper.toDto(savedArticle);
    }

    public AddArticleOutputDto updateArticle(Long id,AddArticleInputDto addArticleInputDto){
        long start = System.nanoTime();
        ArticleEntity articleEntity = articleRepository.findById(id).orElseThrow(()->{
            ArticleNotFoundException exception = new ArticleNotFoundException(id);
            log.warn(LogMessages.ARTICLE_NOT_FOUND,id );
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
                    BrandNotFoundException exception = new BrandNotFoundException(addArticleInputDto.idBrand());
                    log.warn(LogMessages.BRAND_NOT_FOUND, addArticleInputDto.idBrand());
                    return exception;
                });
                articleEntity.setBrand(brandEntity);
            }
        }
        if(addArticleInputDto.idType()!=null){
            if(!Objects.equals(articleEntity.getTypeArticle().getIdTypeArticle(), addArticleInputDto.idType())){
                TypeArticleEntity type=typeArticleRepository.findById(addArticleInputDto.idType()).orElseThrow(()->
                {
                    TypeArticleNotFoundException exception = TypeArticleNotFoundException.byId(addArticleInputDto.idType());
                    log.warn(LogMessages.TYPE_ARTICLE_NOT_FOUND_BY_ID,addArticleInputDto.idType(),exception);
                    return exception;
                });
                    articleEntity.setTypeArticle(type);
            }
        }
        ArticleEntity updatedArticle=articleRepository.save(articleEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("L'article {} a bien été mise à jour,durationMs={}",updatedArticle.getIdArticle(),durationMs);
        return articleMapper.toDto(updatedArticle);
    }

    public void deleteArticle(Long id){
        long start = System.nanoTime();
        ArticleEntity articleEntity=articleRepository.findById(id).orElseThrow(()->
        {
            ArticleNotFoundException exception = new ArticleNotFoundException(id);
            log.warn(LogMessages.ARTICLE_NOT_FOUND,id,exception);
            return exception;
        });
        articleRepository.delete(articleEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("L'article {} a bien été supprimé, durationMs = {}",articleEntity.getIdArticle(),durationMs);
    }

    public List<AddArticleOutputDto> getAllArticles(){
        long start = System.nanoTime();
        List<AddArticleOutputDto> listArticle = articleRepository.findAll().stream().map(article-> articleMapper.toDto(article)).toList();
        long durationMs = Tools.getDurationMs(start);
        log.info("{} articles ont été trouvés, durationMs = {}",listArticle.size(),durationMs);
        return listArticle;
    }

    public AddArticleOutputDto getArticleById(Long id){
        long start = System.nanoTime();
        ArticleEntity articleEntity=articleRepository.findById(id).orElseThrow(()->{
            ArticleNotFoundException exception = new ArticleNotFoundException(id);
            log.warn(LogMessages.ARTICLE_NOT_FOUND,id,exception);
            return exception;
        });
        long durationMs = Tools.getDurationMs(start);
        log.info("L'article {} a bien été trouvé, durationMs = {}",articleEntity.getIdArticle(),durationMs);
        return articleMapper.toDto(articleEntity);
    }
}
