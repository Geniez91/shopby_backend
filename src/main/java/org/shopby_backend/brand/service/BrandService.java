package org.shopby_backend.brand.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.mapper.BrandMapper;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.shopby_backend.exception.brand.*;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class BrandService {
    private BrandRepository brandRepository;
    private BrandMapper brandMapper;

    @Transactional
    public BrandOutputDto addBrand(BrandInputDto brandInputDto) {
        long start = System.nanoTime();

        if(brandRepository.existsByLibelle(brandInputDto.libelle()))
        {
            BrandAlreadyExistsException exception = new BrandAlreadyExistsException(brandInputDto.libelle());
            log.warn(LogMessages.BRAND_ALREADY_EXISTS,brandInputDto.libelle(),exception);
            throw exception;
        };

        BrandEntity brandEntity=brandMapper.toEntity(brandInputDto);
        BrandEntity savedBrand=brandRepository.save(brandEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("La marque {} a bien été ajouté, durationMs {}",savedBrand.getIdBrand(),durationMs);
        return brandMapper.toDto(savedBrand);
    }

    @Transactional
    public BrandOutputDto updateBrand(Long id,BrandInputDto updateBrandInputDto) {
        long start = System.nanoTime();
        if(id==null|| id==0){
            String message = "Le identifiant n'est pas valide";
            BrandUpdateException exception = new BrandUpdateException(message);
            log.warn(message, exception);
            throw exception;
        }

        BrandEntity existBrand = this.findBrandOrThrow(id);

        existBrand.setLibelle(updateBrandInputDto.libelle());
        BrandEntity updatedBrand=brandRepository.save(existBrand);
        long durationMs = Tools.getDurationMs(start);
        log.info("La marque {} a bien été mise à jour, durationMs {}",updatedBrand.getIdBrand(),durationMs);
        return brandMapper.toDto(updatedBrand);
    }

    public Page<BrandOutputDto> findAllBrands(Pageable pageable) {
        long start = System.nanoTime();
        Page<BrandEntity> page= brandRepository.findAll(pageable);
        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe plus de {} marques dans la base de données (page : {}), durationMs {}",page.getNumberOfElements(),page.getNumber(),durationMs);
        return page.map(brandMapper::toDto);
    }

    public BrandOutputDto findBrandById(Long id) {
        long start = System.nanoTime();
        BrandEntity brandEntity = this.findBrandOrThrow(id);
        long durationMs = Tools.getDurationMs(start);
        log.info("Il existe plus une marque avec l'id brand {}, durationMs {}",brandEntity.getIdBrand(),durationMs);
        return brandMapper.toDto(brandEntity);
    }

    @Transactional
    public void deleteBrand(Long id) {
        long start = System.nanoTime();
        BrandEntity brandEntity = this.findBrandOrThrow(id);
        brandRepository.delete(brandEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("La marque {} a bien été supprimé, durationsMs {}",brandEntity.getIdBrand(),durationMs);
    }

    public BrandEntity findBrandOrThrow(Long idBrand){
       return brandRepository.findById(idBrand).orElseThrow(()->
        {
            BrandNotFoundException exception = new BrandNotFoundException(idBrand);
            log.warn(LogMessages.BRAND_NOT_FOUND,idBrand);
            return exception;
        });
    }
}
