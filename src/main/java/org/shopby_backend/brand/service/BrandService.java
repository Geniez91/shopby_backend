package org.shopby_backend.brand.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.service.ArticleService;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.shopby_backend.exception.brand.*;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class BrandService {
    private BrandRepository brandRepository;

    public BrandOutputDto addBrand(BrandInputDto brandInputDto) {
        long start = System.nanoTime();

        if(brandRepository.existsByLibelle(brandInputDto.libelle()))
        {
            BrandAlreadyExistsException exception = new BrandAlreadyExistsException(brandInputDto.libelle());
            log.warn(LogMessages.BRAND_ALREADY_EXISTS,brandInputDto.libelle(),exception);
            throw exception;
        };

        BrandEntity brandEntity=BrandEntity.builder()
                .libelle(brandInputDto.libelle())
                .build();
        BrandEntity savedBrand=brandRepository.save(brandEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("La marque {} a bien été ajouté, durationMs {}",savedBrand.getIdBrand(),durationMs);
        return new BrandOutputDto(savedBrand.getIdBrand(),savedBrand.getLibelle());
    }

    public BrandOutputDto updateBrand(Long id,BrandInputDto updateBrandInputDto) {
        long start = System.nanoTime();
        if(id==null|| id==0){
            String message = "Le identifiant n'est pas valide";
            BrandUpdateException exception = new BrandUpdateException(message);
            log.warn(message, exception);
            throw exception;
        }

        BrandEntity existBrand = brandRepository.findByIdBrand(id).orElseThrow(()->{
            BrandNotFoundException exception = new BrandNotFoundException(id);
            log.warn(LogMessages.BRAND_NOT_FOUND,id,exception);
            return exception;
        });

        existBrand.setLibelle(updateBrandInputDto.libelle());
        BrandEntity updatedBrand=brandRepository.save(existBrand);
        long durationMs = Tools.getDurationMs(start);
        log.info("La marque {} a bien été mise à jour, durationMs {}",updatedBrand.getIdBrand(),durationMs);
        return new BrandOutputDto(updatedBrand.getIdBrand(),updatedBrand.getLibelle());
    }

    public List<BrandOutputDto> findAllBrands() {
        long start = System.nanoTime();

        List<BrandOutputDto> listBrandOutputDto= brandRepository.findAll().stream().map(brandEntity ->
        new BrandOutputDto(brandEntity.getIdBrand(),brandEntity.getLibelle())
        ).toList();

        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe plus de {} marques dans la base de données, durationMs {}",listBrandOutputDto.size(),durationMs);
        return listBrandOutputDto;
    }

    public BrandOutputDto findBrandById(Long id) {
        long start = System.nanoTime();
        BrandEntity brandEntity = brandRepository.findByIdBrand(id).orElseThrow(()->{
            BrandNotFoundException exception = new BrandNotFoundException(id);
            log.warn(LogMessages.BRAND_NOT_FOUND,id,exception);
            return exception;
        });
        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe plus une marque avec l'id brand {}, durationMs {}",brandEntity.getIdBrand(),durationMs);
        return new BrandOutputDto(brandEntity.getIdBrand(),brandEntity.getLibelle());
    }

    public void deleteBrand(Long id) {
        long start = System.nanoTime();
        BrandEntity brandEntity = brandRepository.findByIdBrand(id).orElseThrow(()->{
            BrandNotFoundException exception = new BrandNotFoundException(id);
            log.warn(LogMessages.BRAND_NOT_FOUND,id,exception );
            return exception;
        });
        brandRepository.delete(brandEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("La marque {} a bien été supprimé, durationsMs {}",brandEntity.getIdBrand(),durationMs);
    }
}
