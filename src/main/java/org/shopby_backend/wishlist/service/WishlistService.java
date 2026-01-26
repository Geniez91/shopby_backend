package org.shopby_backend.wishlist.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.exception.wishlist.*;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.shopby_backend.wishlist.dto.*;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.shopby_backend.wishlist.model.WishlistItemEntity;
import org.shopby_backend.wishlist.persistence.WishlistItemRepository;
import org.shopby_backend.wishlist.persistence.WishlistRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@AllArgsConstructor
@Service
public class WishlistService {
    private WishlistRepository wishlistRepository;
    private UsersRepository usersRepository;
    private WishlistItemRepository wishlistItemRepository;
    private ArticleRepository articleRepository;

    public WishlistOutputDto addWishList(WishlistInputDto wishlistInputDto) {
        if(wishlistInputDto.userId()==null){
            throw new WishlistCreateException("L'utilisateur ne peut pas être null");
        }
        UsersEntity user= usersRepository.findById(wishlistInputDto.userId()).orElseThrow(()->new WishlistCreateException("Aucun utilisateur ne correspond à l'id saisie"));
        if(wishlistInputDto.name()==null || wishlistInputDto.name().isBlank()){
            throw new WishlistCreateException("Le nom de votre liste ne peut pas être null ou vide");
        }
        WishlistEntity wishlist= WishlistEntity.builder()
                .user(user)
                .name(wishlistInputDto.name())
                .description(wishlistInputDto.description())
                .build();
        WishlistEntity savedWishList=wishlistRepository.save(wishlist);
        return new WishlistOutputDto(savedWishList.getIdWishlist(), savedWishList.getUser().getId(),savedWishList.getName(),savedWishList.getDescription());
    }

    public WishlistOutputDto updateWishlist(Integer idWishList, WishlistUpdateDto wishlistInputDto) {
        WishlistEntity wishlistEntity= wishlistRepository.findById(idWishList).orElseThrow(()-> new WishlistUpdateException("L'id de la liste d'envie ne correspond a aucun liste"));
        if(wishlistInputDto.name()!=null){
            wishlistEntity.setName(wishlistInputDto.name());
        }
        if(wishlistInputDto.description()!=null){
            wishlistEntity.setDescription(wishlistInputDto.description());
        }
        WishlistEntity savedWishList=wishlistRepository.save(wishlistEntity);
        return new WishlistOutputDto(savedWishList.getIdWishlist(), savedWishList.getUser().getId(),savedWishList.getName(),savedWishList.getDescription());
    }

    public WishlistOutputDto deleteWishlist(Integer idWishList){
        WishlistEntity wishlistEntity= wishlistRepository.findById(idWishList).orElseThrow(()-> new WishlistDeleteException("L'id de la liste d'envie ne correspond a aucun liste"));
        wishlistRepository.delete(wishlistEntity);
        return new WishlistOutputDto(wishlistEntity.getIdWishlist(), wishlistEntity.getUser().getId(),wishlistEntity.getName(),wishlistEntity.getDescription());
    }

    public WishlistOutputDto getWishlist(Integer idWishList){
       WishlistEntity wishlistEntity=wishlistRepository.findById(idWishList).orElseThrow(()->new WishlistGetException("L'id de la liste d'envie ne correspond a aucun liste"));
       return new WishlistOutputDto(wishlistEntity.getIdWishlist(), wishlistEntity.getUser().getId(),wishlistEntity.getName(),wishlistEntity.getDescription());
    }

    public List<WishlistOutputDto>getAllWishListByUserId(WishListGetAllByIdDto wishListGetAllByIdDto){
        if(wishListGetAllByIdDto.userId()==null){
            throw new WishlistGetAllByUserIdException("L'id de l'utilisateur ne peut pas être null");
        }
        UsersEntity user=usersRepository.findById(wishListGetAllByIdDto.userId()).orElseThrow(()->new WishlistGetAllByUserIdException("L'id de l'utilisateur ne correspond à aucun utilisateur"));
        return wishlistRepository.findByUserId(wishListGetAllByIdDto.userId()).stream().map(wishlistEntity -> {
            return new WishlistOutputDto(wishlistEntity.getIdWishlist(), wishlistEntity.getUser().getId(),wishlistEntity.getName(),wishlistEntity.getDescription());
        }).toList();
    }

    public WishlistAddItemOutputDto addWishListItem(Long idWishList, WishlistAddItemInputDto wishlistAddItemInputDto){
        if(idWishList==null){
            throw new WishlistAddItemException("L'id de la liste d'envie ne doit pas être null");
        }
        if(wishlistAddItemInputDto.idArticle()==null){
            throw new WishlistAddItemException("L'id de l'article ne doit pas être null");
        }
        WishlistEntity wishlistEntity=wishlistRepository.findById(Math.toIntExact(idWishList)).orElseThrow(()->new WishlistAddItemException("L'id de la liste d'envie ne correpond a aucune liste d'envie"));
        ArticleEntity articleEntity=articleRepository.findById(wishlistAddItemInputDto.idArticle()).orElseThrow(()-> new WishlistAddItemException("L'id de l'article ne correpond a aucun article"));
        WishlistItemEntity wishlistItemEntity=wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(idWishList, wishlistAddItemInputDto.idArticle());
        if(wishlistItemEntity!=null) {
            throw new WishlistAddItemException("L'article est deja dans votre liste d'envie");
        }
        WishlistItemEntity wishlistItem2=WishlistItemEntity.builder()
                  .article(articleEntity)
                  .wishlist(wishlistEntity)
                  .build();
        WishlistItemEntity savedWishlistItem=wishlistItemRepository.save(wishlistItem2);
        return new WishlistAddItemOutputDto(savedWishlistItem.getWishlist().getIdWishlist(), savedWishlistItem.getArticle().getIdArticle(),savedWishlistItem.getWishlist().getUser().getId(),savedWishlistItem.getWishlist().getName(),savedWishlistItem.getWishlist().getDescription());
    }

    public WishlistAddItemOutputDto deleteWishlistItem(Long idWishList, WishlistAddItemInputDto wishlistAddItemInputDto){
        if(idWishList==null){
            throw new WishlistRemoveItemException("L'id de la liste d'envie ne doit pas être null");
        }
        if(wishlistAddItemInputDto.idArticle()==null){
            throw new WishlistRemoveItemException("L'id de l'article ne doit pas être null");
        }
        WishlistEntity wishlistEntity=wishlistRepository.findById(Math.toIntExact(idWishList)).orElseThrow(()->new WishlistAddItemException("L'id de la liste d'envie ne correpond a aucune liste d'envie"));
        ArticleEntity articleEntity=articleRepository.findById(wishlistAddItemInputDto.idArticle()).orElseThrow(()-> new WishlistAddItemException("L'id de l'article ne correpond a aucun article"));
        WishlistItemEntity wishlistItemEntity=wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(idWishList, wishlistAddItemInputDto.idArticle());
        wishlistRepository.delete(wishlistEntity);
        return new WishlistAddItemOutputDto(wishlistItemEntity.getWishlist().getIdWishlist(), wishlistItemEntity.getArticle().getIdArticle(),wishlistEntity.getUser().getId(),wishlistItemEntity.getWishlist().getName(),wishlistItemEntity.getWishlist().getDescription());
    }

    public List<AddArticleOutputDto> getAllArticleByWishlistId(Long idWishList){
        if(idWishList==null){
            throw new WishlistGetAllArticleException("L'id de l'article ne doit pas être null");
        }
        List<WishlistItemEntity> wishlistItemEntityList=wishlistItemRepository.findByWishlist_id(idWishList);
        if(wishlistItemEntityList==null){
            throw new WishlistGetAllArticleException("L'id de la liste d'envie n'a aucune liste d'envie ou ne contient aucun article");
        }
        return wishlistItemEntityList.stream().map((wishlistItem)->{
            return new AddArticleOutputDto(wishlistItem.getArticle().getIdArticle(),wishlistItem.getArticle().getName(),wishlistItem.getArticle().getDescription(),wishlistItem.getArticle().getPrice(),wishlistItem.getArticle().getBrand().getLibelle(),wishlistItem.getArticle().getTypeArticle().getLibelle(),wishlistItem.getArticle().getCreationDate());
        }).toList();
    }
}
