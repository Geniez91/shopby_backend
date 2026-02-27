package org.shopby_backend.typeArticle.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.exception.typeArticle.*;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleFilter;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.mapper.TypeArticleMapper;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;
import org.shopby_backend.typeArticle.specification.TypeArticleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class TypeArticleService {
    private TypeArticleRepository typeArticleRepository;
    private TypeArticleMapper typeArticleMapper;

    @Transactional
    public TypeArticleOutputDto addTypeArticle(TypeArticleDto typeArticleDto) {
        long start = System.nanoTime();

        if(typeArticleRepository.existsByLibelle(typeArticleDto.libelle())){
            TypeArticleAlreadyExistsException exception = new TypeArticleAlreadyExistsException(typeArticleDto.libelle());
            log.warn(LogMessages.TYPE_ARTICLE_ALREADY_EXISTS,typeArticleDto.libelle(), exception);
            throw exception;
        };

        TypeArticleEntity typeArticleEntity = typeArticleMapper.toEntity(typeArticleDto);

        Long parentId = null;

        if (typeArticleDto.parentId() != null) {
            TypeArticleEntity parent = this.findTypeArticleByParentIdOrThrow(typeArticleDto.parentId());
            typeArticleEntity.setParent(parent);
            parentId = parent.getIdTypeArticle();
        }

        TypeArticleEntity savedTypeArticle = typeArticleRepository.save(typeArticleEntity);
        long durationsMs = Tools.getDurationMs(start);
        log.info("Le type d'article {} a bien été ajouté, durationMs : {}",savedTypeArticle.getLibelle(),durationsMs);
        return typeArticleMapper.toDto(typeArticleEntity,parentId);
    }

    @Transactional
    public TypeArticleOutputDto updateTypeArticle(Long idType,TypeArticleDto typeArticleDto) {
        long start = System.nanoTime();
        TypeArticleEntity typeArticleEntity=this.findTypeArticleOrThrow(idType);

        typeArticleEntity.setLibelle(typeArticleDto.libelle());
        TypeArticleEntity savedTypeArticle=typeArticleRepository.save(typeArticleEntity);
        long durationsMs = Tools.getDurationMs(start);
        log.info("Le type d'article {} a bien été modifié,durationMs : {}",savedTypeArticle.getLibelle(),durationsMs);
        return typeArticleMapper.toDto(savedTypeArticle,savedTypeArticle.getParent().getIdTypeArticle());
    }

    @Transactional
    public void deleteTypeArticle(Long idTypeArticle) {
        long start = System.nanoTime();
        TypeArticleEntity typeArticleEntity=this.findTypeArticleOrThrow(idTypeArticle);
        typeArticleRepository.delete(typeArticleEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le type d'article {} a bien été supprimé, durationMs : {}",typeArticleEntity.getLibelle(),durationMs);
    }

    public Page<TypeArticleOutputDto> getAllTypeArticle(TypeArticleFilter typeArticleFilter, Pageable pageable) {
        long start = System.nanoTime();
        Specification<TypeArticleEntity> filter= TypeArticleSpecification.witFilters(typeArticleFilter);
        Page<TypeArticleEntity> page = typeArticleRepository.findAll(filter, pageable);
        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe plus de {} type d'article dans la base de données,page : {} durationsMs : {}",page.getNumberOfElements(),page.getNumber(),durationMs);
        return page.map(entity ->
                typeArticleMapper.toDto(entity, entity.getParent().getIdTypeArticle())
        );
    }

    public TypeArticleOutputDto getTypeArticleById(Long idTypeArticle) {
        long start = System.nanoTime();
        TypeArticleEntity typeArticleEntity=this.findTypeArticleOrThrow(idTypeArticle);
        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe bien un type d'article {},durationMs : {}",typeArticleEntity.getLibelle(),durationMs);
        return typeArticleMapper.toDto(typeArticleEntity,typeArticleEntity.getParent().getIdTypeArticle());
    }

    public TypeArticleEntity findTypeArticleOrThrow(Long idTypeArticle) {
       return typeArticleRepository.findById(idTypeArticle).orElseThrow(()->
        {
            TypeArticleNotFoundException exception = TypeArticleNotFoundException.byId(idTypeArticle);
            log.warn(LogMessages.TYPE_ARTICLE_NOT_FOUND_BY_ID,idTypeArticle,exception );
            return exception;
        });
    }

    public TypeArticleEntity findTypeArticleByParentIdOrThrow(Long parentId) {
        return typeArticleRepository.findById(parentId).orElseThrow(() ->
        {
            TypeArticleNotFoundException exception = TypeArticleNotFoundException.byParentId(parentId);
            log.warn(LogMessages.TYPE_ARTICLE_NOT_FOUND_BY_PARENT_ID,parentId, exception);
            return exception;
        });
    }
}
