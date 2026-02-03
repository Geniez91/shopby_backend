package org.shopby_backend.brand.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.article.service.ArticleService;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.shopby_backend.exception.brand.BrandCreateException;
import org.shopby_backend.exception.brand.BrandDeleteException;
import org.shopby_backend.exception.brand.BrandGetException;
import org.shopby_backend.exception.brand.BrandUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BrandService {
    private BrandRepository brandRepository;
    private static final Logger logger = LoggerFactory.getLogger(BrandService.class);

    public BrandOutputDto addBrand(BrandInputDto brandInputDto) {
        long start = System.nanoTime();
        if(brandInputDto.libelle().isBlank()){
            String message = "Le libelle de votre marque ne peut pas être vide";
            BrandCreateException exception = new BrandCreateException(message);
            logger.error(message, exception);
            throw exception;
        }

        brandRepository.findByLibelle(brandInputDto.libelle()).orElseThrow(()->{
            BrandCreateException exception = new BrandCreateException("Le libelle de votre marque existe deja "+brandInputDto.libelle());
            logger.error("Le libelle de votre marque existe deja {}",brandInputDto.libelle(),exception);
            return exception;
        });

        BrandEntity brandEntity=BrandEntity.builder()
                .libelle(brandInputDto.libelle())
                .build();
        BrandEntity savedBrand=brandRepository.save(brandEntity);
        long durationMs = (System.nanoTime()-start) / 1_000_000;
        logger.info("La marque {} a bien été ajouté, durationMs {}",savedBrand.getIdBrand(),durationMs);
        return new BrandOutputDto(savedBrand.getIdBrand(),savedBrand.getLibelle());
    }

    public BrandOutputDto updateBrand(Long id,BrandInputDto updateBrandInputDto) {
        long start = System.nanoTime();
        if(id==null|| id==0){
            String message = "Le identifiant n'est pas valide";
            BrandUpdateException exception = new BrandUpdateException(message);
            logger.error(message, exception);
            throw exception;
        }
        if(updateBrandInputDto.libelle().isBlank()){
            String message= "Le libelle de votre marque n'existe pas";
            BrandUpdateException exception = new BrandUpdateException(message);
            logger.error(message, exception);
            throw exception;
        }

        BrandEntity existBrand = brandRepository.findByIdBrand(id).orElseThrow(()->{
            BrandUpdateException exception = new BrandUpdateException("Le identifiant de la marque n'existe pas "+id);
            logger.error("Le identifiant de la marque n'existe pas {}",id,exception);
            return exception;
        });
        existBrand.setLibelle(updateBrandInputDto.libelle());
        BrandEntity updatedBrand=brandRepository.save(existBrand);
        long durationMs = (System.nanoTime()-start) / 1_000_000;
        logger.info("La marque {} a bien été mise à jour, durationMs {}",updatedBrand.getIdBrand(),durationMs);
        return new BrandOutputDto(updatedBrand.getIdBrand(),updatedBrand.getLibelle());
    }

    public List<BrandOutputDto> findAllBrands() {
        long start = System.nanoTime();

        List<BrandOutputDto> listBrandOutputDto= brandRepository.findAll().stream().map(brandEntity ->
        new BrandOutputDto(brandEntity.getIdBrand(),brandEntity.getLibelle())
        ).toList();

        long durationMs = (System.nanoTime()-start) / 1_000_000;
        logger.info("Il existe plus de {} marques dans la base de données, durationMs {}",listBrandOutputDto.size(),durationMs);
        return listBrandOutputDto;
    }

    public BrandOutputDto findBrandById(Long id) {
        long start = System.nanoTime();
        BrandEntity brandEntity = brandRepository.findByIdBrand(id).orElseThrow(()->{
            BrandGetException exception = new BrandGetException("L'identifiant de la marque n'existe pas "+id);
            logger.error("L'identifiant de la marque n'existe pas {}",id,exception);
            return exception;
        });
        long durationMs = (System.nanoTime()-start) / 1_000_000;
        logger.info("Il existe plus une marque avec l'id brand {}, durationMs {}",brandEntity.getIdBrand(),durationMs);
        return new BrandOutputDto(brandEntity.getIdBrand(),brandEntity.getLibelle());
    }

    public BrandOutputDto deleteBrand(Long id) {
        long start = System.nanoTime();
        BrandEntity brandEntity = brandRepository.findByIdBrand(id).orElseThrow(()->{
            BrandDeleteException exception = new BrandDeleteException("L'id saisie ne correspond à aucune marque "+id);
            logger.error("L'id saisie ne correspond à aucune marque {}",id,exception );
            return exception;
        });
        brandRepository.delete(brandEntity);
        long durationMs = (System.nanoTime()-start) / 1_000_000;
        logger.info("La marque {} a bien été supprimé, durationsMs {}",brandEntity.getIdBrand(),durationMs);
        return new BrandOutputDto(brandEntity.getIdBrand(),brandEntity.getLibelle());
    }
}
