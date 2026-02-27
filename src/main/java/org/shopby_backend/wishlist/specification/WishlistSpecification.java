package org.shopby_backend.wishlist.specification;

import jakarta.persistence.criteria.Predicate;
import org.shopby_backend.wishlist.dto.WishlistFilter;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WishlistSpecification {
    public static Specification<WishlistEntity> withFilters(WishlistFilter filter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.name()!=null&&!filter.name().isBlank()){
                predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%"+filter.name()+"%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
