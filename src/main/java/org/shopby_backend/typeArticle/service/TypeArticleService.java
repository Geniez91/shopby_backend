package org.shopby_backend.typeArticle.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.exception.typeArticle.*;
import org.shopby_backend.status.service.StatusService;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
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

    public TypeArticleOutputDto addTypeArticle(TypeArticleDto typeArticleDto) {
        long start = System.nanoTime();

        typeArticleRepository.findByLibelle(typeArticleDto.libelle()).orElseThrow(()->{
            TypeArticleAlreadyExistsException exception = new TypeArticleAlreadyExistsException("Le type d'article existe deja avec le libelle "+typeArticleDto.libelle());
            log.warn("Le type d'article existe deja avec le libelle {}",typeArticleDto.libelle(), exception);
            return exception;
        });

        TypeArticleEntity typeArticleEntity = TypeArticleEntity.builder()
                .libelle(typeArticleDto.libelle())
                .build();

        Long parentId = null;

        if (typeArticleDto.parentId() != null) {
            TypeArticleEntity parent = typeArticleRepository.findById(typeArticleDto.parentId()).orElseThrow(() ->
            {
                TypeArticleNotFoundException exception = new TypeArticleNotFoundException("Le parent est introuvable avec l'id "+typeArticleDto.parentId());
                log.warn("Le parent est introuvable avec l'id {}",typeArticleDto.parentId(), exception);
                return exception;
            });

            typeArticleEntity.setParent(parent);
            parentId = parent.getIdTypeArticle();
        }

        TypeArticleEntity savedTypeArticle = typeArticleRepository.save(typeArticleEntity);
        long durationsMs = Tools.getDurationMs(start);
        log.info("Le type d'article {} a bien été ajouté, durationMs : {}",savedTypeArticle.getLibelle(),durationsMs);
        return new TypeArticleOutputDto(savedTypeArticle.getIdTypeArticle(), savedTypeArticle.getLibelle(), parentId);
    }

    public TypeArticleOutputDto updateTypeArticle(Long idType,TypeArticleDto typeArticleDto) {
        long start = System.nanoTime();
        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idType).orElseThrow(()->
        {
            TypeArticleNotFoundException exception =  new TypeArticleNotFoundException("Le type d'article n'existe pas avc l'id type "+idType);
            log.warn("Le type d'article n'existe pas avc l'id type {}",idType,exception);
            return exception;
        });

        typeArticleEntity.setLibelle(typeArticleDto.libelle());
        TypeArticleEntity savedTypeArticle=typeArticleRepository.save(typeArticleEntity);
        long durationsMs = Tools.getDurationMs(start);
        log.info("Le type d'article {} a bien été modifié,durationMs : {}",savedTypeArticle.getLibelle(),durationsMs);
        return new TypeArticleOutputDto(savedTypeArticle.getIdTypeArticle(),savedTypeArticle.getLibelle(),savedTypeArticle.getParent().getIdTypeArticle());
    }

    public void deleteTypeArticle(Long idTypeArticle) {
        long start = System.nanoTime();

        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idTypeArticle).orElseThrow(()->
        {
            TypeArticleNotFoundException exception = new TypeArticleNotFoundException("Le type d'article n'existe pas avec l'id type "+idTypeArticle);
            log.warn("Le type d'article n'existe pas avec l'id type {}",idTypeArticle,exception);
            return exception;
        });

        typeArticleRepository.delete(typeArticleEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le type d'article {} a bien été supprimé, durationMs : {}",typeArticleEntity.getLibelle(),durationMs);
    }

    public List<TypeArticleOutputDto> getAllTypeArticle() {
        long start = System.nanoTime();
        List<TypeArticleOutputDto> listTypeArticle = typeArticleRepository.findAll().stream().map((typeArticle)-> new TypeArticleOutputDto(typeArticle.getIdTypeArticle(),typeArticle.getLibelle(),typeArticle.getParent().getIdTypeArticle())).toList();
        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe plus de {} type d'article dans la base de données,durationsMs : {}",listTypeArticle.size(),durationMs);
        return listTypeArticle;
    }

    public TypeArticleOutputDto getTypeArticleById(Long idTypeArticle) {
        long start = System.nanoTime();

        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idTypeArticle).orElseThrow(()->
        {
            TypeArticleNotFoundException exception =  new TypeArticleNotFoundException("Le type d'article n'existe pas avec l'id type " + idTypeArticle);
            log.warn("Le type d'article n'existe pas avec l'id type {}",idTypeArticle,exception);
            return exception;
        });

        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe bien un type d'article {},durationMs : {}",typeArticleEntity.getLibelle(),durationMs);
        return new TypeArticleOutputDto(typeArticleEntity.getIdTypeArticle(),typeArticleEntity.getLibelle(),typeArticleEntity.getParent().getIdTypeArticle());
    }
}
