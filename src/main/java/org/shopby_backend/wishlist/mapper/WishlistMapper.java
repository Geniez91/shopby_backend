package org.shopby_backend.wishlist.mapper;

import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.wishlist.dto.WishlistInputDto;
import org.shopby_backend.wishlist.dto.WishlistOutputDto;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {
    public WishlistEntity toEntity(WishlistInputDto wishlistInputDto, UsersEntity usersEntity){
        return  WishlistEntity.builder()
                .user(usersEntity)
                .name(wishlistInputDto.name())
                .description(wishlistInputDto.description())
                .build();
    }

    public WishlistOutputDto toOutputDto(WishlistEntity entity){
        return new WishlistOutputDto(
                entity.getIdWishlist(),
                entity.getUser().getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getVersion()
        );
    }

}
