package org.shopby_backend.users.specification;

import jakarta.persistence.criteria.Predicate;
import org.shopby_backend.users.dto.UserFilter;
import org.shopby_backend.users.model.UsersEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UsersSpecification {
    public static Specification<UsersEntity> withFilters(UserFilter filter){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.nom()!=null&&!filter.nom().isBlank()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")),"%"+filter.nom()+"%"));
            }

            if(filter.prenom()!=null&&!filter.prenom().isBlank()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("prenom")),"%"+filter.prenom()+"%"));
            }

            if(filter.country()!=null&&!filter.country().isBlank()){
                predicates.add(criteriaBuilder.equal(root.get("country"), filter.country()));
            }

            if(filter.roleId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("role").get("id"), filter.roleId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
