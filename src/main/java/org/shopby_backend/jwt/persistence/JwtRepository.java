package org.shopby_backend.jwt.persistence;

import org.shopby_backend.jwt.model.JwtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<JwtEntity, Long> {
    @Query("FROM JwtEntity j WHERE j.user.email=:email")
    Stream<JwtEntity> findByUserValidToken(String email);

    @Query("FROM JwtEntity j WHERE j.expired=:expired AND j.disabled=:disabled AND j.user.email=:email")
    JwtEntity findByUserValidToken(String email,boolean disabled,boolean expired);

    JwtEntity findByTokenAndDisabledAndExpired(String token, boolean disabled, boolean expired);

    void deleteAllByExpiredAndDisabled(boolean expired, boolean disabled);
}
