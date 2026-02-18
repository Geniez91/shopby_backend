package org.shopby_backend.typeArticle.mapper;

import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;

public class TypeArticleMapper {
    public TypeArticleEntity toEntity(TypeArticleDto typeArticleDto){
        return TypeArticleEntity.builder()
                .libelle(typeArticleDto.libelle())
                .build();
    }

    public TypeArticleOutputDto toDto(TypeArticleEntity typeArticleEntity,Long parentId){
        return new TypeArticleOutputDto(typeArticleEntity.getIdTypeArticle(), typeArticleEntity.getLibelle(), parentId);
    }
}
