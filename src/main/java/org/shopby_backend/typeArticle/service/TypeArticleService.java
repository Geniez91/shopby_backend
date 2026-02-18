package org.shopby_backend.typeArticle.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.exception.typeArticle.*;
import org.shopby_backend.status.service.StatusService;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.mapper.TypeArticleMapper;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class TypeArticleService {
    private TypeArticleRepository typeArticleRepository;
    private TypeArticleMapper typeArticleMapper;

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
            TypeArticleEntity parent = typeArticleRepository.findById(typeArticleDto.parentId()).orElseThrow(() ->
            {
                TypeArticleNotFoundException exception = TypeArticleNotFoundException.byParentId(typeArticleDto.parentId());
                log.warn(LogMessages.TYPE_ARTICLE_NOT_FOUND_BY_PARENT_ID,typeArticleDto.parentId(), exception);
                return exception;
            });

            typeArticleEntity.setParent(parent);
            parentId = parent.getIdTypeArticle();
        }

        TypeArticleEntity savedTypeArticle = typeArticleRepository.save(typeArticleEntity);
        long durationsMs = Tools.getDurationMs(start);
        log.info("Le type d'article {} a bien été ajouté, durationMs : {}",savedTypeArticle.getLibelle(),durationsMs);
        return typeArticleMapper.toDto(typeArticleEntity,parentId);
    }

    public TypeArticleOutputDto updateTypeArticle(Long idType,TypeArticleDto typeArticleDto) {
        long start = System.nanoTime();
        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idType).orElseThrow(()->
        {
            TypeArticleNotFoundException exception = TypeArticleNotFoundException.byId(idType);
            log.warn(LogMessages.TYPE_ARTICLE_NOT_FOUND_BY_ID,idType,exception);
            return exception;
        });

        typeArticleEntity.setLibelle(typeArticleDto.libelle());
        TypeArticleEntity savedTypeArticle=typeArticleRepository.save(typeArticleEntity);
        long durationsMs = Tools.getDurationMs(start);
        log.info("Le type d'article {} a bien été modifié,durationMs : {}",savedTypeArticle.getLibelle(),durationsMs);
        return typeArticleMapper.toDto(savedTypeArticle,savedTypeArticle.getParent().getIdTypeArticle());
    }

    public void deleteTypeArticle(Long idTypeArticle) {
        long start = System.nanoTime();

        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idTypeArticle).orElseThrow(()->
        {
            TypeArticleNotFoundException exception = TypeArticleNotFoundException.byId(idTypeArticle);
            log.warn(LogMessages.TYPE_ARTICLE_NOT_FOUND_BY_ID,idTypeArticle,exception);
            return exception;
        });

        typeArticleRepository.delete(typeArticleEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le type d'article {} a bien été supprimé, durationMs : {}",typeArticleEntity.getLibelle(),durationMs);
    }

    public List<TypeArticleOutputDto> getAllTypeArticle() {
        long start = System.nanoTime();
        List<TypeArticleOutputDto> listTypeArticle = typeArticleRepository.findAll().stream().map((typeArticle)-> typeArticleMapper.toDto(typeArticle,typeArticle.getParent()
                .getIdTypeArticle())).toList();
        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe plus de {} type d'article dans la base de données,durationsMs : {}",listTypeArticle.size(),durationMs);
        return listTypeArticle;
    }

    public TypeArticleOutputDto getTypeArticleById(Long idTypeArticle) {
        long start = System.nanoTime();

        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idTypeArticle).orElseThrow(()->
        {
            TypeArticleNotFoundException exception = TypeArticleNotFoundException.byId(idTypeArticle);
            log.warn(LogMessages.TYPE_ARTICLE_NOT_FOUND_BY_ID,idTypeArticle,exception);
            return exception;
        });

        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe bien un type d'article {},durationMs : {}",typeArticleEntity.getLibelle(),durationMs);
        return typeArticleMapper.toDto(typeArticleEntity,typeArticleEntity.getParent().getIdTypeArticle());
    }
}
