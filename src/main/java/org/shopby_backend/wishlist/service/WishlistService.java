package org.shopby_backend.wishlist.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.exception.wishlist.*;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.shopby_backend.users.service.ValidationService;
import org.shopby_backend.wishlist.dto.*;
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
    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    public WishlistOutputDto addWishList(WishlistInputDto wishlistInputDto) {
        long start = System.nanoTime();
        if(wishlistInputDto.userId()==null){
            String message = "L'utilisateur id ne peut pas être null";
            WishlistCreateException exception = new WishlistCreateException(message);
            logger.error(message, exception);
            throw exception;
        }

        UsersEntity user= usersRepository.findById(wishlistInputDto.userId()).orElseThrow(()->
                {
                    WishlistCreateException exception = new WishlistCreateException("Aucun utilisateur ne correspond à l'id saisie " + wishlistInputDto.userId());
                    logger.error("Aucun utilisateur ne correspond à l'id saisie {}", wishlistInputDto.userId(), exception);
                    return exception;
                }
            );

        if(wishlistInputDto.name()==null || wishlistInputDto.name().isBlank()){
            String message = "Le nom de votre liste ne peut pas être null ou vide";
            WishlistCreateException exception = new WishlistCreateException(message);
            logger.error(message, exception);
            throw exception;
        }

        WishlistEntity wishlist= WishlistEntity.builder()
                .user(user)
                .name(wishlistInputDto.name())
                .description(wishlistInputDto.description())
                .build();
        WishlistEntity savedWishList=wishlistRepository.save(wishlist);
        long durationMs = (System.nanoTime()-start)/1000000;
        logger.info("La liste d'envie a bien été créer avec l'id {}, durationMs={}",savedWishList.getIdWishlist(),durationMs);
        return new WishlistOutputDto(savedWishList.getIdWishlist(), savedWishList.getUser().getId(),savedWishList.getName(),savedWishList.getDescription());
    }

    public WishlistOutputDto updateWishlist(Integer idWishList, WishlistUpdateDto wishlistInputDto) {
        long start = System.nanoTime();
        WishlistEntity wishlistEntity= wishlistRepository.findById(idWishList).orElseThrow(()->
        {
            WishlistUpdateException exception = new WishlistUpdateException("L'id de la liste d'envie ne correspond a aucun liste avec l'id "+idWishList);
            logger.error("L'id de la liste d'envie ne correspond a aucun liste avec l'id {}",idWishList, exception);
            return exception;
        });

        if(wishlistInputDto.name()!=null){
            wishlistEntity.setName(wishlistInputDto.name());
        }

        if(wishlistInputDto.description()!=null){
            wishlistEntity.setDescription(wishlistInputDto.description());
        }

        WishlistEntity savedWishList=wishlistRepository.save(wishlistEntity);
        long durationMs = (System.nanoTime()-start)/1000000;
        logger.info("La liste d'envie {} a bien été modifié, durationMs={}",savedWishList.getIdWishlist(),durationMs);
        return new WishlistOutputDto(savedWishList.getIdWishlist(), savedWishList.getUser().getId(),savedWishList.getName(),savedWishList.getDescription());
    }

    public WishlistOutputDto deleteWishlist(Integer idWishList){
        long start = System.nanoTime();
        WishlistEntity wishlistEntity= wishlistRepository.findById(idWishList).orElseThrow(()->
        {
            WishlistDeleteException exception =  new WishlistDeleteException("L'id de la liste d'envie ne correspond a aucun liste avec l'id "+idWishList);
            logger.error("L'id de la liste d'envie ne correspond a aucun liste avec l'id {}",idWishList, exception);
            return exception;
        });
        wishlistRepository.delete(wishlistEntity);
        long durationMs = (System.nanoTime()-start)/1000000;
        logger.info("La liste d'envie {} a bien été supprimé, durationMs={}",wishlistEntity.getIdWishlist(),durationMs);
        return new WishlistOutputDto(wishlistEntity.getIdWishlist(), wishlistEntity.getUser().getId(),wishlistEntity.getName(),wishlistEntity.getDescription());
    }

    public WishlistOutputDto getWishlist(Integer idWishList){
        long start = System.nanoTime();
       WishlistEntity wishlistEntity=wishlistRepository.findById(idWishList).orElseThrow(()->
       {
           WishlistGetException exception = new WishlistGetException("L'id de la liste d'envie ne correspond a aucun liste avec l'id "+idWishList);
           logger.error("L'id de la liste d'envie ne correspond a aucun liste avec l'id {}",idWishList, exception);
           return exception;
       });
       long durationMs = (System.nanoTime()-start)/1000000;
       logger.info("La liste d'envie {} a bien été affiché, durationMs={}",wishlistEntity.getIdWishlist(),durationMs);
       return new WishlistOutputDto(wishlistEntity.getIdWishlist(), wishlistEntity.getUser().getId(),wishlistEntity.getName(),wishlistEntity.getDescription());
    }

    public List<WishlistOutputDto>getAllWishListByUserId(WishListGetAllByIdDto wishListGetAllByIdDto){
        long start = System.nanoTime();
        if(wishListGetAllByIdDto.userId()==null){
            String message = "L'id de l'utilisateur ne peut pas être null";
            WishlistGetAllByUserIdException exception = new WishlistGetAllByUserIdException(message);
            logger.error(message,exception);
        }

        UsersEntity user=usersRepository.findById(wishListGetAllByIdDto.userId()).orElseThrow(()->
        {
            WishlistGetAllByUserIdException exception = new WishlistGetAllByUserIdException("L'id de l'utilisateur ne correspond à aucun utilisateur avec id "+wishListGetAllByIdDto.userId());
            logger.error("L'id de l'utilisateur ne correspond à aucun utilisateur avec id {}",wishListGetAllByIdDto.userId(),exception);
            return exception;
        });

        List<WishlistOutputDto> listWishOutputDto = wishlistRepository.findByUserId(user.getId()).stream().map(wishlistEntity -> new WishlistOutputDto(wishlistEntity.getIdWishlist(), wishlistEntity.getUser().getId(),wishlistEntity.getName(),wishlistEntity.getDescription())).toList();
        long durationMs = (System.nanoTime()-start)/1000000;
        logger.info("Le nombre de list d'envie est de {} pour l'utilisateur {},durationMs={}",listWishOutputDto.size(),wishListGetAllByIdDto.userId(),durationMs);
        return listWishOutputDto;
    }

    public WishlistAddItemOutputDto addWishListItem(Long idWishList, WishlistAddItemInputDto wishlistAddItemInputDto){
        long start = System.nanoTime();
        if(idWishList==null){
            String message = "L'id de la liste d'envie ne doit pas être null";
            WishlistAddItemException exception = new WishlistAddItemException(message);
            logger.error(message,exception);
            throw exception;
        }

        if(wishlistAddItemInputDto.idArticle()==null){
            String message = "L'id de l'article ne doit pas être null";
            WishlistAddItemException exception = new WishlistAddItemException(message);
            logger.error(message,exception);
            throw exception;
        }

        WishlistEntity wishlistEntity=wishlistRepository.findById(Math.toIntExact(idWishList)).orElseThrow(()->
        {
            WishlistAddItemException exception = new WishlistAddItemException("L'id de la liste d'envie ne correpond a aucune liste d'envie avec l'id "+idWishList);
            logger.error("L'id de la liste d'envie ne correpond a aucune liste d'envie avec l'id {}",idWishList,exception);
            return exception;
        });

        ArticleEntity articleEntity=articleRepository.findById(wishlistAddItemInputDto.idArticle()).orElseThrow(()->
        {
            WishlistAddItemException exception = new WishlistAddItemException("L'id de l'article ne correpond a aucun article avec l'id article "+wishlistAddItemInputDto.idArticle());
            logger.error("L'id de l'article ne correpond a aucun article avec l'id article {}",wishlistAddItemInputDto.idArticle(),exception);
            return exception;
        });

        WishlistItemEntity wishlistItemEntity=wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(idWishList, wishlistAddItemInputDto.idArticle()).orElseThrow(()-> {
            WishlistAddItemException exception = new WishlistAddItemException("L'article est deja dans votre liste d'envie avec l'id wishlist "+idWishList + "et l'id article : "+wishlistAddItemInputDto.idArticle());
            logger.error("L'article est deja dans votre liste d'envie avec l'id wishlist {} et l'id article : {}",idWishList,wishlistAddItemInputDto.idArticle(),exception);
            return exception;
        });

        WishlistItemEntity wishlistItem2=WishlistItemEntity.builder()
                  .article(articleEntity)
                  .wishlist(wishlistEntity)
                  .build();
        WishlistItemEntity savedWishlistItem=wishlistItemRepository.save(wishlistItem2);
        long durationMs = (System.nanoTime()-start)/1000000;
        logger.info("L'article {} a bien été ajouté à la liste d'envie {},durationMs={}",savedWishlistItem.getArticle().getIdArticle(),savedWishlistItem.getWishlist().getIdWishlist(),durationMs);
        return new WishlistAddItemOutputDto(savedWishlistItem.getWishlist().getIdWishlist(), savedWishlistItem.getArticle().getIdArticle(),savedWishlistItem.getWishlist().getUser().getId(),savedWishlistItem.getWishlist().getName(),savedWishlistItem.getWishlist().getDescription());
    }

    public WishlistAddItemOutputDto deleteWishlistItem(Long idWishList, WishlistAddItemInputDto wishlistAddItemInputDto){
        long start=System.nanoTime();
        if(idWishList==null){
            String message = "L'id de la liste d'envie ne doit pas être null";
            WishlistRemoveItemException exception = new WishlistRemoveItemException(message);
            logger.error(message,exception);
            throw exception;
        }

        if(wishlistAddItemInputDto.idArticle()==null){
            String message = "L'id de l'article ne doit pas être null";
            WishlistRemoveItemException exception = new WishlistRemoveItemException(message);
            logger.error(message,exception);
            throw exception;
        }

        WishlistEntity wishlistEntity=wishlistRepository.findById(Math.toIntExact(idWishList)).orElseThrow(()->
        {
            WishlistRemoveItemException exception = new WishlistRemoveItemException("L'id de la liste d'envie ne correpond a aucune liste d'envie avec l'id "+idWishList);
            logger.error("L'id de la liste d'envie ne correpond a aucune liste d'envie avec l'id {}",idWishList,exception );
            return exception;
        });

        ArticleEntity articleEntity=articleRepository.findById(wishlistAddItemInputDto.idArticle()).orElseThrow(()-> {
            WishlistRemoveItemException exception = new WishlistRemoveItemException("L'id de l'article ne correpond a aucun article avec l'id article "+wishlistAddItemInputDto.idArticle());
            logger.error("L'id de l'article ne correpond a aucun article avec l'id article {}",wishlistAddItemInputDto.idArticle(),exception);
            return exception;
        } );

        WishlistItemEntity wishlistItemEntity=wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(idWishList, wishlistAddItemInputDto.idArticle()).orElseThrow(()->{
            WishlistRemoveItemException exception = new WishlistRemoveItemException("L'id de liste d'envie et de l'article ne correpond a aucun article d'une liste d'envie avec idWishlist "+idWishList+ " et idArticle : "+wishlistAddItemInputDto.idArticle());
            logger.error("L'id de liste d'envie et de l'article ne correpond a aucun article d'une liste d'envie avec idWishlist {} et idArticle : {}",idWishList,wishlistAddItemInputDto.idArticle(),exception);
            return exception;
        });

        wishlistRepository.delete(wishlistEntity);
        long durationMs = (System.nanoTime()-start)/1000000;
        logger.info("L'article {} a bien été supprimé de la liste d'envie {}, durationMs={}",articleEntity.getIdArticle(),wishlistItemEntity.getWishlist().getIdWishlist(),durationMs);
        return new WishlistAddItemOutputDto(wishlistItemEntity.getWishlist().getIdWishlist(), articleEntity.getIdArticle(),wishlistEntity.getUser().getId(),wishlistItemEntity.getWishlist().getName(),wishlistItemEntity.getWishlist().getDescription());
    }

    public List<AddArticleOutputDto> getAllArticleByWishlistId(Long idWishList){
        long start = System.nanoTime();
        if(idWishList==null){
            String message = "L'id de l'article ne doit pas être null";
            WishlistGetAllArticleException exception = new WishlistGetAllArticleException(message);
            logger.error(message,exception);
            throw exception;
        }

        List<WishlistItemEntity> wishlistItemEntityList=wishlistItemRepository.findByWishlist_idWishlist(idWishList).orElseThrow(()->{
            WishlistGetAllArticleException exception = new WishlistGetAllArticleException("L'id de la liste d'envie n'a aucune liste d'envie ou ne contient aucun article avec id wishlist : "+idWishList);
            logger.error("L'id de la liste d'envie n'a aucune liste d'envie ou ne contient aucun article avec id wishlist : {}",idWishList,exception);
            return exception;
        });

        List<AddArticleOutputDto> listArticle= wishlistItemEntityList.stream().map((wishlistItem)-> new AddArticleOutputDto(wishlistItem.getArticle().getIdArticle(),wishlistItem.getArticle().getName(),wishlistItem.getArticle().getDescription(),wishlistItem.getArticle().getPrice(),wishlistItem.getArticle().getBrand().getLibelle(),wishlistItem.getArticle().getTypeArticle().getLibelle(),wishlistItem.getArticle().getCreationDate())).toList();
        long durationMs = (System.nanoTime()-start)/1000000;
        logger.info("Le nombre d'article dans la liste d'envie {} est de {},durationMs={}",idWishList,listArticle,durationMs);
        return listArticle;
    }
}
