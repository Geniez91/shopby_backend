package org.shopby_backend.brand.mapper;

import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.model.BrandEntity;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {
    public BrandEntity toEntity(BrandInputDto brandInputDto){
        return BrandEntity.builder()
                .libelle(brandInputDto.libelle())
                .build();
    }

    public BrandOutputDto toDto(BrandEntity brandEntity){
        return new BrandOutputDto(brandEntity.getIdBrand(),brandEntity.getLibelle());
    }
}
