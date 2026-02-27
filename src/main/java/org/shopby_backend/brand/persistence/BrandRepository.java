package org.shopby_backend.brand.persistence;

import org.shopby_backend.brand.model.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<BrandEntity,Long>, JpaSpecificationExecutor<BrandEntity> {
    Optional<BrandEntity> findByIdBrand(Long idBrand);
    boolean existsByLibelle(String libelle);
}
