package org.shopby_backend.wishlist.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.wishlist.dto.*;
import org.shopby_backend.wishlist.service.WishlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    private WishlistService wishlistService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistOutputDto addNewWishlist(@Valid @RequestBody WishlistInputDto wishlistInputDto){
        return wishlistService.addWishList(wishlistInputDto);
    }

    @PatchMapping("/{wishListId}")
    @ResponseStatus(HttpStatus.OK)
    public WishlistOutputDto updateNewWishlist(@PathVariable Integer wishListId, @Valid @RequestBody WishlistUpdateDto wishlistUpdateDto){
        return wishlistService.updateWishlist(wishListId,wishlistUpdateDto);
    }

    @DeleteMapping("/{wishListId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWishlist(@PathVariable Integer wishListId){
         wishlistService.deleteWishlist(wishListId);
    }

    @GetMapping("/{wishListId}")
    @ResponseStatus(HttpStatus.OK)
    public WishlistOutputDto getWishlist(@PathVariable Integer wishListId){
        return wishlistService.getWishlist(wishListId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<WishlistOutputDto> getWishlists(WishlistFilter filter, @Valid @RequestBody WishListGetAllByIdDto wishListGetAllByIdDto,Pageable pageable){
        return wishlistService.getAllWishListByUserId(filter,wishListGetAllByIdDto,pageable);
    }

    @PostMapping("/{wishlistId}")
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistAddItemOutputDto addNewListItem(@PathVariable Long wishlistId,@Valid @RequestBody WishlistAddItemInputDto wishlistAddItemInputDto){
        return wishlistService.addWishListItem(wishlistId,wishlistAddItemInputDto);
    }

    @DeleteMapping("/{wishlistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWishlistItem(@PathVariable Long wishlistId,@Valid @RequestBody WishlistAddItemInputDto wishlistAddItemInputDto){
         wishlistService.deleteWishlistItem(wishlistId,wishlistAddItemInputDto);
    }

    @GetMapping("{wishlistId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<AddArticleOutputDto> getAllArticlesByWishlistId(@PathVariable Long wishlistId, Pageable pageable){
        return wishlistService.getAllArticleByWishlistId(wishlistId,pageable);
    }
}
