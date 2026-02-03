package org.shopby_backend.users.persistence;

import org.shopby_backend.users.model.ValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationRepository extends JpaRepository<ValidationEntity,Long> {
    Optional<ValidationEntity> findByCode(String code);
}
