package org.shopby_backend.users.persistence;

import org.shopby_backend.users.model.ValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationRepository extends JpaRepository<ValidationEntity,Long> {
    ValidationEntity findByCode(String code);
}
