package org.shopby_backend.typeArticle.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.exception.typeArticle.TypeArticleAddException;
import org.shopby_backend.exception.typeArticle.TypeArticleDeleteException;
import org.shopby_backend.exception.typeArticle.TypeArticleGetException;
import org.shopby_backend.exception.typeArticle.TypeArticleUpdateException;
import org.shopby_backend.status.service.StatusService;
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
    private static final Logger logger = LoggerFactory.getLogger(TypeArticleService.class);


    public TypeArticleOutputDto addTypeArticle(TypeArticleDto typeArticleDto) {
        long start = System.nanoTime();
        if (typeArticleDto == null) {
            String message = "Le type d'article ne peut pas être null";
            TypeArticleAddException exception = new TypeArticleAddException("Le type d'article ne peut pas être null");
            logger.error(message, exception);
            throw exception;
        }

        if (typeArticleDto.libelle() == null) {
            String message = "Le libelle du type d'article ne peut pas être null";
            TypeArticleAddException exception = new TypeArticleAddException(message);
            logger.error(message, exception);
            throw exception;
        }

        TypeArticleEntity alreadyTypeArticle = typeArticleRepository.findByLibelle(typeArticleDto.libelle()).orElseThrow(()->{
            TypeArticleAddException exception = new TypeArticleAddException("Le type d'article existe deja avec le libelle "+typeArticleDto.libelle());
            logger.error("Le type d'article existe deja avec le libelle {}",typeArticleDto.libelle(), exception);
            throw exception;
        });

        TypeArticleEntity typeArticleEntity = TypeArticleEntity.builder()
                .libelle(typeArticleDto.libelle())
                .build();

        Long parentId = null;

        if (typeArticleDto.parentId() != null) {
            TypeArticleEntity parent = typeArticleRepository.findById(typeArticleDto.parentId()).orElseThrow(() ->
            {
                TypeArticleAddException exception = new TypeArticleAddException("Le parent est introuvable avec l'id "+typeArticleDto.parentId());
                logger.error("Le parent est introuvable avec l'id {}",typeArticleDto.parentId(), exception);
                return exception;
            });

            typeArticleEntity.setParent(parent);
            parentId = parent.getIdTypeArticle();
        }

        TypeArticleEntity savedTypeArticle = typeArticleRepository.save(typeArticleEntity);
        long durationsMs = (System.nanoTime() - start)/1000000;
        logger.info("Le type d'article {} a bien été ajouté, durationMs : {}",savedTypeArticle.getLibelle(),durationsMs);
        return new TypeArticleOutputDto(savedTypeArticle.getIdTypeArticle(), savedTypeArticle.getLibelle(), parentId);
    }

    public TypeArticleOutputDto updateTypeArticle(Long idType,TypeArticleDto typeArticleDto) {
        long start = System.nanoTime();
        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idType).orElseThrow(()->
        {
            TypeArticleUpdateException exception =  new TypeArticleUpdateException("Le type d'article n'existe pas avc l'id type "+idType);
            logger.error("Le type d'article n'existe pas avc l'id type {}",idType,exception);
            return exception;
        });

        if (typeArticleDto.libelle() == null) {
            String message = "Le libelle ne peut pas être vide";
            TypeArticleUpdateException exception = new TypeArticleUpdateException(message);
            logger.error(message, exception);
            throw exception;
        }

        typeArticleEntity.setLibelle(typeArticleDto.libelle());
        TypeArticleEntity savedTypeArticle=typeArticleRepository.save(typeArticleEntity);
        long durationsMs = (System.nanoTime() - start)/1000000;
        logger.info("Le type d'article {} a bien été modifié,durationMs : {}",savedTypeArticle.getLibelle(),durationsMs);
        return new TypeArticleOutputDto(savedTypeArticle.getIdTypeArticle(),savedTypeArticle.getLibelle(),savedTypeArticle.getParent().getIdTypeArticle());
    }

    public TypeArticleOutputDto deleteTypeArticle(Long idTypeArticle) {
        long start = System.nanoTime();

        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idTypeArticle).orElseThrow(()->
        {
            TypeArticleDeleteException exception = new TypeArticleDeleteException("Le type d'article n'existe pas avec l'id type "+idTypeArticle);
            logger.error("Le type d'article n'existe pas avec l'id type {}",idTypeArticle,exception);
            return exception;
        });

        typeArticleRepository.delete(typeArticleEntity);
        long durationMs = (System.nanoTime() - start)/1000000;
        logger.info("Le type d'article {} a bien été supprimé, durationMs : {}",typeArticleEntity.getLibelle(),durationMs);
        return new TypeArticleOutputDto(typeArticleEntity.getIdTypeArticle(),typeArticleEntity.getLibelle(),typeArticleEntity.getParent().getIdTypeArticle());
    }

    public List<TypeArticleOutputDto> getAllTypeArticle() {
        long start = System.nanoTime();
        List<TypeArticleOutputDto> listTypeArticle = typeArticleRepository.findAll().stream().map((typeArticle)-> new TypeArticleOutputDto(typeArticle.getIdTypeArticle(),typeArticle.getLibelle(),typeArticle.getParent().getIdTypeArticle())).toList();
        long durationMs = (System.nanoTime() - start)/1000000;
        logger.info("Il existe plus de {} type d'article dans la base de données,durationsMs : {}",listTypeArticle.size(),durationMs);
        return listTypeArticle;
    }

    public TypeArticleOutputDto getTypeArticleById(Long idTypeArticle) {
        long start = System.nanoTime();

        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idTypeArticle).orElseThrow(()->
        {
            TypeArticleGetException exception =  new TypeArticleGetException("Le type d'article n'existe pas avec l'id type " + idTypeArticle);
            logger.error("Le type d'article n'existe pas avec l'id type {}",idTypeArticle,exception);
            return exception;
        });

        long durationMs = (System.nanoTime() - start)/1000000;
        logger.info("Il existe bien un type d'article {},durationMs : {}",typeArticleEntity.getLibelle(),durationMs);
        return new TypeArticleOutputDto(typeArticleEntity.getIdTypeArticle(),typeArticleEntity.getLibelle(),typeArticleEntity.getParent().getIdTypeArticle());
    }
}
