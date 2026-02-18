package org.shopby_backend.status.mapper;

import org.shopby_backend.status.dto.StatusInputDto;
import org.shopby_backend.status.dto.StatusOutputDto;
import org.shopby_backend.status.model.StatusEntity;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {
    public StatusEntity toEntity(StatusInputDto statusInputDto) {
        return StatusEntity.builder()
                .libelle(statusInputDto.libelle())
                .build();
    }

    public StatusOutputDto toDto(StatusEntity statusEntity) {
        return new StatusOutputDto(statusEntity.getIdStatus(), statusEntity.getLibelle());
    }
}
