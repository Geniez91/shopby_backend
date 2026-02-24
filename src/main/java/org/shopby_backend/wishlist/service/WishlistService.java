package org.shopby_backend.wishlist.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.mapper.ArticleMapper;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.article.service.ArticleService;
import org.shopby_backend.exception.article.ArticleNotFoundException;
import org.shopby_backend.exception.users.UsersNotFoundException;
import org.shopby_backend.exception.wishlist.*;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.shopby_backend.users.service.UsersService;
import org.shopby_backend.users.service.ValidationService;
import org.shopby_backend.wishlist.dto.*;
import org.shopby_backend.wishlist.mapper.WishlistItemMapper;
import org.shopby_backend.wishlist.mapper.WishlistMapper;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.shopby_backend.wishlist.model.WishlistItemEntity;
import org.shopby_backend.wishlist.persistence.WishlistItemRepository;
import org.shopby_backend.wishlist.persistence.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final WishlistMapper wishlistMapper;
    private final WishlistItemMapper wishlistItemMapper;
    private final UsersService usersService;
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @Transactional
    public WishlistOutputDto addWishList(WishlistInputDto wishlistInputDto) {
        long start = System.nanoTime();

        UsersEntity user= this.usersService.findUsersOrThrow(wishlistInputDto.userId());
        WishlistEntity wishlist = wishlistMapper.toEntity(wishlistInputDto,user);
        WishlistEntity savedWishList=wishlistRepository.save(wishlist);
        long durationMs = Tools.getDurationMs(start);
        log.info("La liste d'envie a bien été créer avec l'id {}, durationMs={}",savedWishList.getIdWishlist(),durationMs);
        return wishlistMapper.toOutputDto(savedWishList);
    }

    @Transactional
    public WishlistOutputDto updateWishlist(Integer idWishList, WishlistUpdateDto wishlistInputDto) {
        long start = System.nanoTime();
        WishlistEntity wishlistEntity= this.findWishlistByIdOrThrow(idWishList);

        if(wishlistInputDto.name()!=null){
            wishlistEntity.setName(wishlistInputDto.name());
        }

        if(wishlistInputDto.description()!=null){
            wishlistEntity.setDescription(wishlistInputDto.description());
        }

        WishlistEntity savedWishList=wishlistRepository.save(wishlistEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("La liste d'envie {} a bien été modifié, durationMs={}",savedWishList.getIdWishlist(),durationMs);
        return wishlistMapper.toOutputDto(savedWishList);
    }

    @Transactional
    public void deleteWishlist(Integer idWishList){
        long start = System.nanoTime();
        WishlistEntity wishlistEntity= this.findWishlistByIdOrThrow(idWishList);
        wishlistRepository.delete(wishlistEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("La liste d'envie {} a bien été supprimé, durationMs={}",wishlistEntity.getIdWishlist(),durationMs);
    }

    public WishlistOutputDto getWishlist(Integer idWishList){
        long start = System.nanoTime();
        WishlistEntity wishlistEntity= this.findWishlistByIdOrThrow(idWishList);
       long durationMs = Tools.getDurationMs(start);
        log.info("La liste d'envie {} a bien été affiché, durationMs={}",wishlistEntity.getIdWishlist(),durationMs);
       return wishlistMapper.toOutputDto(wishlistEntity);
    }

    public Page<WishlistOutputDto>getAllWishListByUserId(WishListGetAllByIdDto wishListGetAllByIdDto,Pageable pageable){
        long start = System.nanoTime();
        UsersEntity user=this.usersService.findUsersOrThrow(wishListGetAllByIdDto.userId());
        Page<WishlistEntity> page = wishlistRepository.findByUserId(user.getId(),pageable);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre de list d'envie est de {}, page : {} pour l'utilisateur {},durationMs={}",page.getNumberOfElements(),page.getNumber(),wishListGetAllByIdDto.userId(),durationMs);
        return page.map(wishlistMapper::toOutputDto);
    }

    @Transactional
    public WishlistAddItemOutputDto addWishListItem(Long idWishList, WishlistAddItemInputDto wishlistAddItemInputDto){
        long start = System.nanoTime();
        if(idWishList==null){
            String message = "L'id de la liste d'envie ne doit pas être null";
            WishlistAddItemException exception = new WishlistAddItemException(message);
            log.warn(message,exception);
            throw exception;
        }

        WishlistEntity wishlistEntity=this.findWishlistByIdOrThrow(Math.toIntExact(idWishList));

        ArticleEntity articleEntity=articleService.findArticleOrThrow(wishlistAddItemInputDto.idArticle());

        if(wishlistItemRepository.existsByWishlist_IdWishlistAndArticle_IdArticle(idWishList, wishlistAddItemInputDto.idArticle())){
            WishlistItemAlreadyExistsException exception = new WishlistItemAlreadyExistsException(idWishList,wishlistAddItemInputDto.idArticle());
            log.warn(LogMessages.ARTICLE_ID_ALREADY_EXISTS_IN_WISHLIST_ID,idWishList,wishlistAddItemInputDto.idArticle(),exception);
            throw exception;
        };

        WishlistItemEntity wishlistItem2 = wishlistItemMapper.toEntity(articleEntity,wishlistEntity);
        WishlistItemEntity savedWishlistItem=wishlistItemRepository.save(wishlistItem2);
        long durationMs = Tools.getDurationMs(start);
        log.info("L'article {} a bien été ajouté à la liste d'envie {},durationMs={}",savedWishlistItem.getArticle().getIdArticle(),savedWishlistItem.getWishlist().getIdWishlist(),durationMs);
        return wishlistItemMapper.toAddDto(savedWishlistItem);
    }

    @Transactional
    public void deleteWishlistItem(Long idWishList, WishlistAddItemInputDto wishlistAddItemInputDto){
        long start=System.nanoTime();
        this.findWishlistByIdOrThrow(Math.toIntExact(idWishList));
        ArticleEntity articleEntity=articleService.findArticleOrThrow(wishlistAddItemInputDto.idArticle());
        WishlistItemEntity wishlistItemEntity=this.findWishlistItemByIdWishlistAndIdArticleOrThrow(idWishList,wishlistAddItemInputDto.idArticle());
        wishlistItemRepository.delete(wishlistItemEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("L'article {} a bien été supprimé de la liste d'envie {}, durationMs={}",articleEntity.getIdArticle(),wishlistItemEntity.getWishlist().getIdWishlist(),durationMs);
    }

    public Page<AddArticleOutputDto> getAllArticleByWishlistId(Long idWishList,Pageable pageable){
        long start = System.nanoTime();
        Page<WishlistItemEntity> wishlistItemEntityList=this.findWishlistItemsByWishlistIdOrThrow(idWishList,pageable);
        Page<AddArticleOutputDto> listArticle= wishlistItemEntityList.map((wishlistItem)-> new AddArticleOutputDto(wishlistItem.getArticle().getIdArticle(),wishlistItem.getArticle().getName(),wishlistItem.getArticle().getDescription(),wishlistItem.getArticle().getPrice(),wishlistItem.getArticle().getBrand().getLibelle(),wishlistItem.getArticle().getTypeArticle().getLibelle(),wishlistItem.getArticle().getCreationDate(),wishlistItem.getWishlist().getVersion()));
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre d'article dans la liste d'envie {} est de {},page : {},durationMs={}",idWishList,listArticle.getNumberOfElements(),listArticle.getNumber(),durationMs);
        return listArticle;
    }

    public WishlistEntity findWishlistByIdOrThrow(int idWishlist){
        return wishlistRepository.findById(idWishlist).orElseThrow(()->
        {
            WishlistNotFoundException exception = new WishlistNotFoundException(idWishlist);
            log.warn(LogMessages.WISHLIST_NOT_FOUND_BY_ID,idWishlist, exception);
            return exception;
        });
    }

    public WishlistItemEntity findWishlistItemByIdWishlistAndIdArticleOrThrow(Long idWishlist,Long idArticle){
        return wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(idWishlist, idArticle).orElseThrow(()->{
            WishlistRemoveItemException exception = new WishlistRemoveItemException(idWishlist,idArticle);
            log.warn(LogMessages.WISHLIST_ITEM_NOT_FOUND_BY_WISHLIST_ID_AND_ARTICLE_ID,idWishlist,idArticle,exception);
            return exception;
        });
    }

    public Page<WishlistItemEntity> findWishlistItemsByWishlistIdOrThrow(Long wishlistId, Pageable pageable){
      return wishlistItemRepository.findByWishlist_idWishlist(wishlistId,pageable).orElseThrow(()->{
            WishlistNotFoundException exception = new WishlistNotFoundException(Math.toIntExact(wishlistId));
            log.warn(LogMessages.WISHLIST_NOT_FOUND_BY_ID,wishlistId,exception);
            return exception;
        });
    }
}
