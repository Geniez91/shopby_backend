package org.shopby_backend.typeArticle.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.exception.typeArticle.TypeArticleAddException;
import org.shopby_backend.exception.typeArticle.TypeArticleDeleteException;
import org.shopby_backend.exception.typeArticle.TypeArticleGetException;
import org.shopby_backend.exception.typeArticle.TypeArticleUpdateException;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.typeArticle.persistence.TypeArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TypeArticleService {
    private TypeArticleRepository typeArticleRepository;

    public TypeArticleOutputDto addTypeArticle(TypeArticleDto typeArticleDto) {
        if (typeArticleDto.libelle() == null) {
            throw new TypeArticleAddException("Le libelle du type d'article ne peut pas être null");
        }
        TypeArticleEntity alreadyTypeArticle = typeArticleRepository.findByLibelle(typeArticleDto.libelle());
        if (alreadyTypeArticle != null) {
            throw new TypeArticleAddException("Le type d'article existe deja");
        }
        Long parentId=null;
        TypeArticleEntity typeArticleEntity = TypeArticleEntity
                .builder()
                .libelle(typeArticleDto.libelle())
                .build();
        if (typeArticleDto.parentId() != null) {
            TypeArticleEntity parent=typeArticleRepository.findById(typeArticleDto.parentId()).orElseThrow(null);
            typeArticleEntity.setParent(parent);
            parentId=typeArticleEntity.getParent().getIdTypeArticle();
        }
        TypeArticleEntity savedTypeArticle=typeArticleRepository.save(typeArticleEntity);
        return new TypeArticleOutputDto(savedTypeArticle.getIdTypeArticle(),savedTypeArticle.getLibelle(),parentId);
    }

    public TypeArticleOutputDto updateTypeArticle(Long idType,TypeArticleDto typeArticleDto) {
        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idType).orElseThrow(()->new TypeArticleUpdateException("Le type d'article n'existe pas"));
        if (typeArticleDto.libelle() == null) {
            throw new TypeArticleUpdateException("Le libelle ne peut pas être vide");
        }
        typeArticleEntity.setLibelle(typeArticleDto.libelle());
        TypeArticleEntity savedTypeArticle=typeArticleRepository.save(typeArticleEntity);
        return new TypeArticleOutputDto(savedTypeArticle.getIdTypeArticle(),savedTypeArticle.getLibelle(),savedTypeArticle.getParent().getIdTypeArticle());
    }

    public TypeArticleOutputDto deleteTypeArticle(Long idTypeArticle) {
        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idTypeArticle).orElseThrow(()->new TypeArticleDeleteException("Le type d'article n'existe pas"));
        typeArticleRepository.delete(typeArticleEntity);
        return new TypeArticleOutputDto(typeArticleEntity.getIdTypeArticle(),typeArticleEntity.getLibelle(),typeArticleEntity.getParent().getIdTypeArticle());
    }

    public List<TypeArticleOutputDto> getAllTypeArticle() {
        return typeArticleRepository.findAll().stream().map((typeArticle)->{
            return new TypeArticleOutputDto(typeArticle.getIdTypeArticle(),typeArticle.getLibelle(),typeArticle.getParent().getIdTypeArticle());
        }).toList();
    }

    public TypeArticleOutputDto getTypeArticleById(Long idTypeArticle) {
        TypeArticleEntity typeArticleEntity=typeArticleRepository.findById(idTypeArticle).orElseThrow(()-> new TypeArticleGetException("Le type d'article n'existe pas"));
        return new TypeArticleOutputDto(typeArticleEntity.getIdTypeArticle(),typeArticleEntity.getLibelle(),typeArticleEntity.getParent().getIdTypeArticle());
    }
}
