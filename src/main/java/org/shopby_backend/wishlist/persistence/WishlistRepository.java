package org.shopby_backend.wishlist.persistence;

import org.shopby_backend.wishlist.dto.WishlistFilter;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Integer>, JpaSpecificationExecutor<WishlistEntity> {
    Page<WishlistEntity> findByUserId(Specification<WishlistEntity> specification,Long userId, Pageable pageable);
}
