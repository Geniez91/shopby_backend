package org.shopby_backend.typeArticle.specification;

import jakarta.persistence.criteria.Predicate;
import org.shopby_backend.typeArticle.dto.TypeArticleFilter;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TypeArticleSpecification {
    public static Specification<TypeArticleEntity>witFilters(TypeArticleFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(filter.libelle()!=null&&!filter.libelle().isBlank()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("libelle")), "%"+filter.libelle()+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
