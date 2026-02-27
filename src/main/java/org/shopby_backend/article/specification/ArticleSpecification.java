package org.shopby_backend.article.specification;

import jakarta.persistence.criteria.Predicate;
import org.shopby_backend.article.dto.ArticleFilter;
import org.shopby_backend.article.model.ArticleEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ArticleSpecification {
    public static Specification<ArticleEntity>withFilters(ArticleFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            /// permet d'ajouter des filtres a notre requetes
            /// ici on ajoute le filtre minPrice en filtrant que le prix est inférieur aux prix
            if(filter.minPrice()!=null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"),filter.minPrice()));
            }

            /// ici on ajoute le filtre maxPrice en filtrant que le prix est supérieur aux prix
            if(filter.maxPrice()!=null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"),filter.maxPrice()));
            }


            if(filter.averageRating()!=null){
                predicates.add(criteriaBuilder.equal(root.get("averageRating"),filter.averageRating()));
            }

           if(filter.brandId()!=null){
               predicates.add(criteriaBuilder.equal(root.get("brand").get("idBrand"),filter.brandId()));
           }

           if(filter.typeArticleId()!=null){
               predicates.add(criteriaBuilder.equal(root.get("typeArticle").get("idTypeArticle"),filter.typeArticleId()));
           }

           if(filter.name()!=null&& !filter.name().isBlank()){
               predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%"+filter.name().toLowerCase()+"%"));
           }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
