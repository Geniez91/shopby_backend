package org.shopby_backend.wishlist.persistence;

import org.shopby_backend.wishlist.model.WishlistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Integer> {
    Page<WishlistEntity> findByUserId(Long userId, Pageable pageable);
}
