package org.shopby_backend.wishlist.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.exception.article.ArticleNotFoundException;
import org.shopby_backend.exception.users.UsersNotFoundException;
import org.shopby_backend.exception.wishlist.*;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
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
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class WishlistService {
    private WishlistRepository wishlistRepository;
    private UsersRepository usersRepository;
    private WishlistItemRepository wishlistItemRepository;
    private ArticleRepository articleRepository;
    private WishlistMapper wishlistMapper;
    private WishlistItemMapper wishlistItemMapper;
    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    public WishlistOutputDto addWishList(WishlistInputDto wishlistInputDto) {
        long start = System.nanoTime();

        UsersEntity user= usersRepository.findById(wishlistInputDto.userId()).orElseThrow(()->
                {
                    UsersNotFoundException exception = UsersNotFoundException.byUserId(wishlistInputDto.userId());
                    logger.warn(LogMessages.USERS_NOT_FOUND_BY_USER_ID, wishlistInputDto.userId(), exception);
                    return exception;
                }
            );

        WishlistEntity wishlist = wishlistMapper.toEntity(wishlistInputDto,user);
        WishlistEntity savedWishList=wishlistRepository.save(wishlist);
        long durationMs = Tools.getDurationMs(start);
        logger.info("La liste d'envie a bien été créer avec l'id {}, durationMs={}",savedWishList.getIdWishlist(),durationMs);
        return wishlistMapper.toOutputDto(savedWishList);
    }

    public WishlistOutputDto updateWishlist(Integer idWishList, WishlistUpdateDto wishlistInputDto) {
        long start = System.nanoTime();
        WishlistEntity wishlistEntity= wishlistRepository.findById(idWishList).orElseThrow(()->
        {
            WishlistNotFoundException exception = new WishlistNotFoundException(idWishList);
            logger.warn(LogMessages.WISHLIST_NOT_FOUND_BY_ID,idWishList, exception);
            return exception;
        });

        if(wishlistInputDto.name()!=null){
            wishlistEntity.setName(wishlistInputDto.name());
        }

        if(wishlistInputDto.description()!=null){
            wishlistEntity.setDescription(wishlistInputDto.description());
        }

        WishlistEntity savedWishList=wishlistRepository.save(wishlistEntity);
        long durationMs = Tools.getDurationMs(start);
        logger.info("La liste d'envie {} a bien été modifié, durationMs={}",savedWishList.getIdWishlist(),durationMs);
        return wishlistMapper.toOutputDto(savedWishList);
    }

    public void deleteWishlist(Integer idWishList){
        long start = System.nanoTime();
        WishlistEntity wishlistEntity= wishlistRepository.findById(idWishList).orElseThrow(()->
        {
            WishlistNotFoundException exception =  new WishlistNotFoundException(idWishList);
            logger.warn(LogMessages.WISHLIST_NOT_FOUND_BY_ID,idWishList, exception);
            return exception;
        });
        wishlistRepository.delete(wishlistEntity);
        long durationMs = Tools.getDurationMs(start);
        logger.info("La liste d'envie {} a bien été supprimé, durationMs={}",wishlistEntity.getIdWishlist(),durationMs);
    }

    public WishlistOutputDto getWishlist(Integer idWishList){
        long start = System.nanoTime();
       WishlistEntity wishlistEntity=wishlistRepository.findById(idWishList).orElseThrow(()->
       {
           WishlistNotFoundException exception = new WishlistNotFoundException(idWishList);
           logger.warn(LogMessages.WISHLIST_NOT_FOUND_BY_ID,idWishList, exception);
           return exception;
       });
       long durationMs = Tools.getDurationMs(start);
       logger.info("La liste d'envie {} a bien été affiché, durationMs={}",wishlistEntity.getIdWishlist(),durationMs);
       return wishlistMapper.toOutputDto(wishlistEntity);
    }

    public List<WishlistOutputDto>getAllWishListByUserId(WishListGetAllByIdDto wishListGetAllByIdDto){
        long start = System.nanoTime();

        UsersEntity user=usersRepository.findById(wishListGetAllByIdDto.userId()).orElseThrow(()->
        {
            UsersNotFoundException exception = UsersNotFoundException.byUserId(wishListGetAllByIdDto.userId());
            logger.warn(LogMessages.USERS_NOT_FOUND_BY_USER_ID,wishListGetAllByIdDto.userId(),exception);
            return exception;
        });

        List<WishlistOutputDto> listWishOutputDto = wishlistRepository.findByUserId(user.getId()).stream().map(wishlistEntity -> wishlistMapper.toOutputDto(wishlistEntity)).toList();
        long durationMs = Tools.getDurationMs(start);
        logger.info("Le nombre de list d'envie est de {} pour l'utilisateur {},durationMs={}",listWishOutputDto.size(),wishListGetAllByIdDto.userId(),durationMs);
        return listWishOutputDto;
    }

    public WishlistAddItemOutputDto addWishListItem(Long idWishList, WishlistAddItemInputDto wishlistAddItemInputDto){
        long start = System.nanoTime();
        if(idWishList==null){
            String message = "L'id de la liste d'envie ne doit pas être null";
            WishlistAddItemException exception = new WishlistAddItemException(message);
            logger.warn(message,exception);
            throw exception;
        }

        WishlistEntity wishlistEntity=wishlistRepository.findById(Math.toIntExact(idWishList)).orElseThrow(()->
        {
            WishlistNotFoundException exception = new WishlistNotFoundException(Math.toIntExact(idWishList));
            logger.warn(LogMessages.WISHLIST_NOT_FOUND_BY_ID,idWishList,exception);
            return exception;
        });

        ArticleEntity articleEntity=articleRepository.findById(wishlistAddItemInputDto.idArticle()).orElseThrow(()->
        {
            ArticleNotFoundException exception = new ArticleNotFoundException(wishlistAddItemInputDto.idArticle());
            logger.warn(LogMessages.ARTICLE_NOT_FOUND,wishlistAddItemInputDto.idArticle(),exception);
            return exception;
        });

        if(wishlistItemRepository.existsByWishlist_IdWishlistAndArticle_IdArticle(idWishList, wishlistAddItemInputDto.idArticle())){
            WishlistItemAlreadyExistsException exception = new WishlistItemAlreadyExistsException(idWishList,wishlistAddItemInputDto.idArticle());
            logger.warn(LogMessages.ARTICLE_ID_ALREADY_EXISTS_IN_WISHLIST_ID,idWishList,wishlistAddItemInputDto.idArticle(),exception);
            throw  exception;
        };

        WishlistItemEntity wishlistItem2 = wishlistItemMapper.toEntity(articleEntity,wishlistEntity);
        WishlistItemEntity savedWishlistItem=wishlistItemRepository.save(wishlistItem2);
        long durationMs = Tools.getDurationMs(start);
        logger.info("L'article {} a bien été ajouté à la liste d'envie {},durationMs={}",savedWishlistItem.getArticle().getIdArticle(),savedWishlistItem.getWishlist().getIdWishlist(),durationMs);
        return wishlistItemMapper.toAddDto(savedWishlistItem);
    }

    public void deleteWishlistItem(Long idWishList, WishlistAddItemInputDto wishlistAddItemInputDto){
        long start=System.nanoTime();

        WishlistEntity wishlistEntity=wishlistRepository.findById(Math.toIntExact(idWishList)).orElseThrow(()->
        {
            WishlistNotFoundException exception = new WishlistNotFoundException(Math.toIntExact(idWishList));
            logger.warn(LogMessages.WISHLIST_NOT_FOUND_BY_ID,idWishList,exception );
            return exception;
        });

        ArticleEntity articleEntity=articleRepository.findById(wishlistAddItemInputDto.idArticle()).orElseThrow(()-> {
            ArticleNotFoundException exception = new ArticleNotFoundException(wishlistAddItemInputDto.idArticle());
            logger.warn(LogMessages.ARTICLE_NOT_FOUND,wishlistAddItemInputDto.idArticle(),exception);
            return exception;
        } );

        WishlistItemEntity wishlistItemEntity=wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(idWishList, wishlistAddItemInputDto.idArticle()).orElseThrow(()->{
            WishlistRemoveItemException exception = new WishlistRemoveItemException(idWishList,wishlistAddItemInputDto.idArticle());
            logger.warn(LogMessages.WISHLIST_ITEM_NOT_FOUND_BY_WISHLIST_ID_AND_ARTICLE_ID,idWishList,wishlistAddItemInputDto.idArticle(),exception);
            return exception;
        });

        wishlistItemRepository.delete(wishlistItemEntity);
        long durationMs = Tools.getDurationMs(start);
        logger.info("L'article {} a bien été supprimé de la liste d'envie {}, durationMs={}",articleEntity.getIdArticle(),wishlistItemEntity.getWishlist().getIdWishlist(),durationMs);
    }

    public List<AddArticleOutputDto> getAllArticleByWishlistId(Long idWishList){
        long start = System.nanoTime();

        List<WishlistItemEntity> wishlistItemEntityList=wishlistItemRepository.findByWishlist_idWishlist(idWishList).orElseThrow(()->{
            WishlistNotFoundException exception = new WishlistNotFoundException(Math.toIntExact(idWishList));
            logger.warn(LogMessages.WISHLIST_NOT_FOUND_BY_ID,idWishList,exception);
            return exception;
        });

        List<AddArticleOutputDto> listArticle= wishlistItemEntityList.stream().map((wishlistItem)-> new AddArticleOutputDto(wishlistItem.getArticle().getIdArticle(),wishlistItem.getArticle().getName(),wishlistItem.getArticle().getDescription(),wishlistItem.getArticle().getPrice(),wishlistItem.getArticle().getBrand().getLibelle(),wishlistItem.getArticle().getTypeArticle().getLibelle(),wishlistItem.getArticle().getCreationDate())).toList();
        long durationMs = Tools.getDurationMs(start);
        logger.info("Le nombre d'article dans la liste d'envie {} est de {},durationMs={}",idWishList,listArticle,durationMs);
        return listArticle;
    }
}
