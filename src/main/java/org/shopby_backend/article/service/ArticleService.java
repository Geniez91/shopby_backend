package org.shopby_backend.article.service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.mapper.ArticleMapper;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.brand.service.BrandService;
import org.shopby_backend.exception.article.*;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.typeArticle.service.TypeArticleService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final BrandService brandService;
    private final TypeArticleService typeArticleService;

    @Transactional
    public AddArticleOutputDto addNewArticle(AddArticleInputDto addArticleInputDto){
        long start = System.nanoTime();
        if(articleRepository.existsByName(addArticleInputDto.nameArticle())){
            ArticleAlreadyExistsException exception = new ArticleAlreadyExistsException(addArticleInputDto.nameArticle());
            log.warn(LogMessages.ARTICLE_ALREADY_EXISTS,addArticleInputDto.nameArticle());
            throw exception;
        }

        BrandEntity brandEntity = brandService.findBrandOrThrow(addArticleInputDto.idBrand());

        TypeArticleEntity typeArticleEntity=typeArticleService.findTypeArticleOrThrow(addArticleInputDto.idType());

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

    @Transactional
    public AddArticleOutputDto updateArticle(Long id,AddArticleInputDto addArticleInputDto){
        long start = System.nanoTime();
        ArticleEntity articleEntity = this.findArticleOrThrow(id);

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
                BrandEntity brandEntity= brandService.findBrandOrThrow(addArticleInputDto.idBrand());
                articleEntity.setBrand(brandEntity);
            }
        }
        if(addArticleInputDto.idType()!=null){
            if(!Objects.equals(articleEntity.getTypeArticle().getIdTypeArticle(), addArticleInputDto.idType())){
                TypeArticleEntity type=typeArticleService.findTypeArticleOrThrow(addArticleInputDto.idType());
                    articleEntity.setTypeArticle(type);
            }
        }
        ArticleEntity updatedArticle=articleRepository.save(articleEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("L'article {} a bien été mise à jour,durationMs={}",updatedArticle.getIdArticle(),durationMs);
        return articleMapper.toDto(updatedArticle);
    }

    @Transactional
    public void deleteArticle(Long id){
        long start = System.nanoTime();
        ArticleEntity articleEntity=this.findArticleOrThrow(id);
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
        ArticleEntity articleEntity=this.findArticleOrThrow(id);
        long durationMs = Tools.getDurationMs(start);
        log.info("L'article {} a bien été trouvé, durationMs = {}",articleEntity.getIdArticle(),durationMs);
        return articleMapper.toDto(articleEntity);
    }

    public ArticleEntity findArticleOrThrow(Long id){
        return articleRepository.findById(id).orElseThrow(()->{
            ArticleNotFoundException exception = new ArticleNotFoundException(id);
            log.warn(LogMessages.ARTICLE_NOT_FOUND,id );
            return exception;
        });
    }
}
