package org.shopby_backend.brand.persistence;

import org.shopby_backend.brand.model.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandEntity,Long> {
    BrandEntity findByIdBrand(Long idBrand);
    BrandEntity findByLibelle(String libelle);
}
