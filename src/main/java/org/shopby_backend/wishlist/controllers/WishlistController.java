package org.shopby_backend.wishlist.controllers;

import lombok.AllArgsConstructor;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.wishlist.dto.*;
import org.shopby_backend.wishlist.service.WishlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class WishlistController {
    private WishlistService wishlistService;

    @PostMapping("/wishlist")
    public WishlistOutputDto addNewWishlist(@RequestBody WishlistInputDto wishlistInputDto){
        return wishlistService.addWishList(wishlistInputDto);
    }

    @PatchMapping("/wishlist/{wishListId}")
    public WishlistOutputDto updateNewWishlist(@PathVariable Integer wishListId, @RequestBody WishlistUpdateDto wishlistUpdateDto){
        return wishlistService.updateWishlist(wishListId,wishlistUpdateDto);
    }

    @DeleteMapping("/wishlist/{wishListId}")
    public WishlistOutputDto deleteWishlist(@PathVariable Integer wishListId){
        return wishlistService.deleteWishlist(wishListId);
    }

    @GetMapping("/wishlist/{wishListId}")
    public WishlistOutputDto getWishlist(@PathVariable Integer wishListId){
        return wishlistService.getWishlist(wishListId);
    }

    @GetMapping("/wishlist")
    public List<WishlistOutputDto> getWishlists(@RequestBody WishListGetAllByIdDto wishListGetAllByIdDto){
        return wishlistService.getAllWishListByUserId(wishListGetAllByIdDto);
    }

    @PostMapping("/wishlist/{wishlistId}")
    public WishlistAddItemOutputDto addNewListItem(@PathVariable Long wishlistId,@RequestBody WishlistAddItemInputDto wishlistAddItemInputDto){
        return wishlistService.addWishListItem(wishlistId,wishlistAddItemInputDto);
    }

    @DeleteMapping("/wishlist/{wishlistId}")
    public WishlistAddItemOutputDto deleteWishlistItem(@PathVariable Long wishlistId,@RequestBody WishlistAddItemInputDto wishlistAddItemInputDto){
        return wishlistService.deleteWishlistItem(wishlistId,wishlistAddItemInputDto);
    }

    @GetMapping("/wishlist/{wishlistId}")
    public List<AddArticleOutputDto> getAllArticlesByWishlistId(@PathVariable Long wishlistId){
        return wishlistService.getAllArticleByWishlistId(wishlistId);
    }
}
