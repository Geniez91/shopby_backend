package org.shopby_backend.jwt.persistence;

import org.shopby_backend.jwt.model.JwtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<JwtEntity, Long> {
    @Query("FROM JwtEntity j WHERE j.user.email=:email")
    Stream<JwtEntity> findByUserValidToken(String email);

    JwtEntity findByTokenAndDisabledAndExpired(String token, boolean disabled, boolean expired);
}
