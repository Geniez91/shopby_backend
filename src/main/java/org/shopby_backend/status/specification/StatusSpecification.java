package org.shopby_backend.status.specification;

import jakarta.persistence.criteria.Predicate;
import org.shopby_backend.status.dto.StatusFilter;
import org.shopby_backend.status.model.StatusEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StatusSpecification {
    public static Specification<StatusEntity> witFilters(StatusFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.libelle() != null && !filter.libelle().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("libelle")), filter.libelle().toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
