package org.shopby_backend.users.persistence;

import org.shopby_backend.users.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Long>, JpaSpecificationExecutor<UsersEntity> {
    Optional<UsersEntity> findByEmail(String email);
}
