package org.shopby_backend.article.mapper;

import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.springframework.stereotype.Component;

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
                articleEntity.getTypeArticle().getLibelle(),
                articleEntity.getBrand().getLibelle(),
                articleEntity.getCreationDate());
    };
}
