package org.shopby_backend.article.mapper;

import org.shopby_backend.article.dto.*;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.articlePhoto.dto.ArticlePhotoOutputDto;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleMapper {
    public ArticleEntity toEntity(AddArticleInputDto addArticleInputDto, TypeArticleEntity typeArticleEntity, BrandEntity brandEntity) {
        return ArticleEntity.builder()
                .name(addArticleInputDto.nameArticle())
                .description(addArticleInputDto.descriptionArticle())
                .brand(brandEntity)
                .typeArticle(typeArticleEntity)
                .price(addArticleInputDto.price())
                .build();
    }

    public AddArticleOutputDto toDto(ArticleEntity articleEntity) {
        return new AddArticleOutputDto(
                articleEntity.getIdArticle(),
                articleEntity.getName(),
                articleEntity.getDescription(),
                articleEntity.getPrice(),
                articleEntity.getBrand().getLibelle(),
                articleEntity.getTypeArticle().getLibelle(),
                articleEntity.getCreationDate(),
                articleEntity.getVersion(),
                articleEntity.getAverageRating(),
                articleEntity.getRatingCount()
        );
    };

    public GetArticlesOutputDto toGetArticlesDto(ArticleEntity articleEntity,String coverUrl) {
        return new GetArticlesOutputDto(
                articleEntity.getIdArticle(),
                articleEntity.getName(),
                articleEntity.getDescription(),
                articleEntity.getPrice(),
                articleEntity.getBrand().getLibelle(),
                articleEntity.getTypeArticle().getLibelle(),
                articleEntity.getCreationDate(),
                articleEntity.getVersion(),
                articleEntity.getAverageRating(),
                articleEntity.getRatingCount(),
                coverUrl
        );
    }

    public GetArticleOutputDto toGetDto(ArticleEntity articleEntity, List<ArticlePhotoOutputDto> articlePhotoOutputDto, List<String> breadcrumDto) {
        return new GetArticleOutputDto(
                articleEntity.getIdArticle(),
                articleEntity.getName(),
                articleEntity.getDescription(),
                articleEntity.getPrice(),
                articleEntity.getBrand().getLibelle(),
                articleEntity.getTypeArticle().getLibelle(),
                articleEntity.getCreationDate(),
                articleEntity.getVersion(),
                articleEntity.getAverageRating(),
                articleEntity.getRatingCount(),
                articlePhotoOutputDto,
                breadcrumDto
        );
    }
}
