package org.shopby_backend.brand.specification;

import jakarta.persistence.criteria.Predicate;
import org.shopby_backend.brand.dto.BrandFilter;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.model.BrandEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BrandSpecification {
    public static Specification<BrandEntity>withFilters(BrandFilter brandFilter){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(brandFilter.libelle()!=null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("libelle")), brandFilter.libelle()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
