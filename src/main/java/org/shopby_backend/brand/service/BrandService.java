package org.shopby_backend.brand.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.brand.persistence.BrandRepository;
import org.shopby_backend.exception.brand.BrandCreateException;
import org.shopby_backend.exception.brand.BrandDeleteException;
import org.shopby_backend.exception.brand.BrandGetException;
import org.shopby_backend.exception.brand.BrandUpdateException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BrandService {
    private BrandRepository brandRepository;

    public BrandOutputDto addBrand(BrandInputDto brandInputDto) {
        if(brandInputDto.libelle().isBlank()){
            throw new BrandCreateException("Le libelle de votre marque n'est pas valide");
        }
        BrandEntity alreadyExists = brandRepository.findByLibelle(brandInputDto.libelle());
        if(alreadyExists != null){
            throw new BrandCreateException("Le libelle de votre marque existe deja");
        }

        BrandEntity brandEntity=BrandEntity.builder()
                .libelle(brandInputDto.libelle())
                .build();
        BrandEntity savedBrand=brandRepository.save(brandEntity);
        return new BrandOutputDto(savedBrand.getIdBrand(),savedBrand.getLibelle());
    }

    public BrandOutputDto updateBrand(Long id,BrandInputDto updateBrandInputDto) {
        if(id==null|| id==0){
            throw new BrandUpdateException("Le identifiant n'est pas valide");
        }
        if(updateBrandInputDto.libelle().isBlank()){
            throw new BrandUpdateException("Le libelle de votre marque n'existe pas");
        }
        BrandEntity existBrand = brandRepository.findByIdBrand(id);
        if(existBrand==null){
            throw new BrandUpdateException("La marque saisie n'existe pas");
        }
        existBrand.setLibelle(updateBrandInputDto.libelle());
        BrandEntity updatedBrand=brandRepository.save(existBrand);
        return new BrandOutputDto(updatedBrand.getIdBrand(),updatedBrand.getLibelle());
    }

    public List<BrandOutputDto> findAllBrands() {
        return brandRepository.findAll().stream().map(brandEntity ->
        new BrandOutputDto(brandEntity.getIdBrand(),brandEntity.getLibelle())
        ).toList();
    }

    public BrandOutputDto findBrandById(Long id) {
        BrandEntity brandEntity = brandRepository.findByIdBrand(id);
        if(brandEntity==null){
            throw new BrandGetException("L'id saisie ne correspond à aucune marque");
        }
        return new BrandOutputDto(brandEntity.getIdBrand(),brandEntity.getLibelle());
    }

    public BrandOutputDto deleteBrand(Long id) {
        BrandEntity brandEntity = brandRepository.findByIdBrand(id);
        if(brandEntity==null){
            throw new BrandDeleteException("L'id saisie ne correspond à aucune marque");
        }
        brandRepository.delete(brandEntity);
        return new BrandOutputDto(brandEntity.getIdBrand(),brandEntity.getLibelle());
    }
}
