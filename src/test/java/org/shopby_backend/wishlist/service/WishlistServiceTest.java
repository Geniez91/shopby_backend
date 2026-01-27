package org.shopby_backend.wishlist.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.article.service.ArticleService;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.exception.wishlist.*;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.shopby_backend.wishlist.dto.*;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.shopby_backend.wishlist.model.WishlistItemEntity;
import org.shopby_backend.wishlist.persistence.WishlistItemRepository;
import org.shopby_backend.wishlist.persistence.WishlistRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {
    @Mock
    ArticleRepository articleRepository;

    @Mock
    WishlistRepository wishlistRepository;

    @Mock
    WishlistItemRepository wishlistItemRepository;

    @Mock
    UsersRepository usersRepository;

    @InjectMocks
    WishlistService wishlistService;

    @Test
    void shouldAddNewWishlist() {
        WishlistInputDto wishlistInputDto=new WishlistInputDto(1L,"liste d'envie","description");
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name(wishlistInputDto.name())
                .description(wishlistInputDto.description())
                .build();
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(wishlistRepository.save(any(WishlistEntity.class))).thenReturn(wishlistEntity);

        WishlistOutputDto wishlistOutputDto = wishlistService.addWishList(wishlistInputDto);

        Assertions.assertEquals("liste d'envie",wishlistOutputDto.name());
    }

    @Test
    void shouldThrowAnExceptionAddNewWishlistWhenUserIdIsEmpty() {
        WishlistInputDto wishlistInputDto=new WishlistInputDto(null,"liste d'envie","description");

        WishlistCreateException wishlistCreateException=assertThrows(WishlistCreateException.class,()->{
            wishlistService.addWishList(wishlistInputDto);
        });

        Assertions.assertEquals("L'utilisateur ne peut pas être null",wishlistCreateException.getMessage());
    }
    @Test
    void shouldThrowAnExceptionAddNewWishlistWhenUserNotFound() {
        WishlistInputDto wishlistInputDto=new WishlistInputDto(1L,"liste d'envie","description");
        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        WishlistCreateException wishlistCreateException=assertThrows(WishlistCreateException.class,()->{
            wishlistService.addWishList(wishlistInputDto);
        });

        Assertions.assertEquals("Aucun utilisateur ne correspond à l'id saisie",wishlistCreateException.getMessage());
    }

    @Test
    void shouldThrowAnExceptionAddNewWishlistWhenNameIsEmpty() {
        WishlistInputDto wishlistInputDto=new WishlistInputDto(1L,null,"description");
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        WishlistCreateException wishlistCreateException=assertThrows(WishlistCreateException.class,()->{
            wishlistService.addWishList(wishlistInputDto);
        });

        Assertions.assertEquals("Le nom de votre liste ne peut pas être null ou vide",wishlistCreateException.getMessage());
    }

    @Test
    void shouldUpdateWishlist() {
        WishlistUpdateDto wishlistInputDto=new WishlistUpdateDto("liste d'envie","description");
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        WishlistEntity updatedWishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name(wishlistInputDto.name())
                .description(wishlistInputDto.description())
                .build();
        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));
        when(wishlistRepository.save(any(WishlistEntity.class))).thenReturn(updatedWishlistEntity);

        WishlistOutputDto wishlistOutputDto = wishlistService.updateWishlist(1,wishlistInputDto);

        Assertions.assertEquals("liste d'envie",wishlistOutputDto.name());

    }

    @Test
    void shouldThrownAnExceptionUpdateWishlistWhenWishlistNotFound(){
        WishlistUpdateDto wishlistInputDto=new WishlistUpdateDto("liste d'envie","description");
        when(wishlistRepository.findById(1)).thenReturn(Optional.empty());

        WishlistUpdateException wishlistUpdateException=assertThrows(WishlistUpdateException.class,()->{
            wishlistService.updateWishlist(1,wishlistInputDto);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste",wishlistUpdateException.getMessage());
    }

    @Test
    void shouldDeleteWishlist(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));

        WishlistOutputDto wishlistOutputDto =  wishlistService.deleteWishlist(1);

        Assertions.assertEquals("name",wishlistOutputDto.name());
    }

    @Test
    void shouldThrownAnExceptionDeleteWishlistWhenWishlistNotFound(){
        when(wishlistRepository.findById(1)).thenReturn(Optional.empty());

        WishlistDeleteException wishlistDeleteException=assertThrows(WishlistDeleteException.class,()->{
            wishlistService.deleteWishlist(1);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste",wishlistDeleteException.getMessage());
    }

    @Test
    void shouldGetWishlistById(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));

        WishlistOutputDto wishlistOutputDto = wishlistService.getWishlist(1);
        Assertions.assertEquals("name",wishlistOutputDto.name());
    }

    @Test
    void shouldThrowAnExceptionGetWishlistByIdWhenWishlistNotFound(){
        when(wishlistRepository.findById(1)).thenReturn(Optional.empty());

        WishlistGetException wishlistGetException=assertThrows(WishlistGetException.class,()->{
            wishlistService.getWishlist(1);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste",wishlistGetException.getMessage());
    }

    @Test
    void shouldGetAllWishlistsByUser(){
        WishListGetAllByIdDto wishListGetAllByIdDto = new WishListGetAllByIdDto(1L);
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        WishlistEntity wishlistEntity2=WishlistEntity.builder()
                .idWishlist(2L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        List<WishlistEntity> wishlistEntityList= List.of(wishlistEntity,wishlistEntity2);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(wishlistRepository.findByUserId(user.getId())).thenReturn(wishlistEntityList);

        List<WishlistOutputDto> wishlistOutputDtos = wishlistService.getAllWishListByUserId(wishListGetAllByIdDto);

        Assertions.assertEquals("name",wishlistOutputDtos.get(0).name());
        Assertions.assertEquals("description",wishlistOutputDtos.get(0).description());
        Assertions.assertEquals("name",wishlistOutputDtos.get(1).name());
        Assertions.assertEquals("description",wishlistOutputDtos.get(1).description());
    }

    @Test
    void shouldThrownAnExceptionGetAllWishlistWhenNoUserId(){
        WishListGetAllByIdDto wishListGetAllByIdDto = new WishListGetAllByIdDto(null);

        WishlistGetAllByUserIdException wishlistGetAllByUserIdException=assertThrows(WishlistGetAllByUserIdException.class,()->{
            wishlistService.getAllWishListByUserId(wishListGetAllByIdDto);
        });

        Assertions.assertEquals("L'id de l'utilisateur ne peut pas être null", wishlistGetAllByUserIdException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionGetAllWishlistWhenNoUserFound(){
        WishListGetAllByIdDto wishListGetAllByIdDto = new WishListGetAllByIdDto(1L);
        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        WishlistGetAllByUserIdException wishlistGetAllByUserIdException=assertThrows(WishlistGetAllByUserIdException.class,()->{
            wishlistService.getAllWishListByUserId(wishListGetAllByIdDto);
        });

        Assertions.assertEquals("L'id de l'utilisateur ne correspond à aucun utilisateur", wishlistGetAllByUserIdException.getMessage());
    }

    @Test
    void shouldAddWishlistItemToWishlist(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        WishlistAddItemInputDto wishlistAddItemInputDto= new WishlistAddItemInputDto(1L);
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        WishlistItemEntity wishlistItem=WishlistItemEntity.builder()
                .article(articleEntity)
                .wishlist(wishlistEntity)
                .build();
        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));
        when(articleRepository.findById(wishlistAddItemInputDto.idArticle())).thenReturn(Optional.of(articleEntity));
        when(wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(wishlistEntity.getIdWishlist(),articleEntity.getIdArticle())).thenReturn(null);
        when(wishlistItemRepository.save(any(WishlistItemEntity.class))).thenReturn(wishlistItem);

        WishlistAddItemOutputDto wishlistAddItemOutputDto= wishlistService.addWishListItem(1L,wishlistAddItemInputDto);

        Assertions.assertEquals("name",wishlistAddItemOutputDto.name());
        Assertions.assertEquals("description",wishlistAddItemOutputDto.description());

    }

    @Test
    void shouldThrownAnExceptionAddWishlistItemWhenNoArticleId(){
        WishlistAddItemInputDto wishlistAddItemInputDto= new WishlistAddItemInputDto(null);

        WishlistAddItemException wishlistAddItemException=assertThrows(WishlistAddItemException.class,()->{
            wishlistService.addWishListItem(1L,wishlistAddItemInputDto);
        });
        Assertions.assertEquals("L'id de l'article ne doit pas être null", wishlistAddItemException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionAddWishlistItemWhenNoWishlistId(){
        WishlistAddItemInputDto wishlistAddItemInputDto= new WishlistAddItemInputDto(1L);

        WishlistAddItemException wishlistAddItemException=assertThrows(WishlistAddItemException.class,()->{
            wishlistService.addWishListItem(null,wishlistAddItemInputDto);
        });
        Assertions.assertEquals("L'id de la liste d'envie ne doit pas être null", wishlistAddItemException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionAddWishlistItemWhenWishlistNotFound(){
        WishlistAddItemInputDto wishlistAddItemInputDto= new WishlistAddItemInputDto(1L);
        when(wishlistRepository.findById(1)).thenReturn(Optional.empty());

        WishlistAddItemException wishlistAddItemException=assertThrows(WishlistAddItemException.class,()->{
            wishlistService.addWishListItem(1L,wishlistAddItemInputDto);
        });
        Assertions.assertEquals("L'id de la liste d'envie ne correpond a aucune liste d'envie", wishlistAddItemException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionAddWishlistItemWhenArticleNotFound(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        WishlistAddItemInputDto wishlistAddItemInputDto= new WishlistAddItemInputDto(1L);

        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));


        WishlistAddItemException wishlistAddItemException=assertThrows(WishlistAddItemException.class,()->{
            wishlistService.addWishListItem(1L,wishlistAddItemInputDto);
        });
        Assertions.assertEquals("L'id de l'article ne correpond a aucun article", wishlistAddItemException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionAddWishlistItemWhenWishlistAlreadyExists(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        WishlistAddItemInputDto wishlistAddItemInputDto= new WishlistAddItemInputDto(1L);
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        WishlistItemEntity wishlistItem=WishlistItemEntity.builder()
                .article(articleEntity)
                .wishlist(wishlistEntity)
                .build();
        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));
        when(articleRepository.findById(wishlistAddItemInputDto.idArticle())).thenReturn(Optional.of(articleEntity));
        when(wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(wishlistEntity.getIdWishlist(),articleEntity.getIdArticle())).thenReturn(wishlistItem);

        WishlistAddItemException wishlistAddItemException=assertThrows(WishlistAddItemException.class,()->{
            wishlistService.addWishListItem(1L,wishlistAddItemInputDto);
        });

        Assertions.assertEquals("L'article est deja dans votre liste d'envie", wishlistAddItemException.getMessage());
    }

    @Test
    void shouldDeleteArticleFromWishlist(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        WishlistAddItemInputDto wishlistAddItemInputDto=new WishlistAddItemInputDto(1L);
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        WishlistItemEntity wishlistItem=WishlistItemEntity.builder()
                .article(articleEntity)
                .wishlist(wishlistEntity)
                .build();
        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));
        when(articleRepository.findById(wishlistAddItemInputDto.idArticle())).thenReturn(Optional.of(articleEntity));
        when(wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(1L,wishlistAddItemInputDto.idArticle())).thenReturn(wishlistItem);

        WishlistAddItemOutputDto wishlistAddItemOutputDto = wishlistService.deleteWishlistItem(1L,wishlistAddItemInputDto);

        Assertions.assertEquals(1L,wishlistAddItemOutputDto.idWishlist());
    }

    @Test
    void shouldThrownAnExceptionDeleteArticleWhenIdWishlistIsNull(){
        WishlistAddItemInputDto wishlistAddItemInputDto=new WishlistAddItemInputDto(1L);

        WishlistRemoveItemException wishlistRemoveItemException=assertThrows(WishlistRemoveItemException.class,()->{
            wishlistService.deleteWishlistItem(null,wishlistAddItemInputDto);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne doit pas être null",wishlistRemoveItemException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionDeleteArticleWhenIdArticleIsNull(){
        WishlistAddItemInputDto wishlistAddItemInputDto=new WishlistAddItemInputDto(null);

        WishlistRemoveItemException wishlistRemoveItemException=assertThrows(WishlistRemoveItemException.class,()->{
            wishlistService.deleteWishlistItem(1L,wishlistAddItemInputDto);
        });

        Assertions.assertEquals("L'id de l'article ne doit pas être null",wishlistRemoveItemException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionDeleteArticleWhenWishlistNotFound(){
        WishlistAddItemInputDto wishlistAddItemInputDto=new WishlistAddItemInputDto(1L);
        when(wishlistRepository.findById(1)).thenReturn(Optional.empty());

        WishlistRemoveItemException wishlistRemoveItemException=assertThrows(WishlistRemoveItemException.class,()->{
            wishlistService.deleteWishlistItem(1L,wishlistAddItemInputDto);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne correpond a aucune liste d'envie",wishlistRemoveItemException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionDeleteArticleWhenArticleNotFound(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        WishlistAddItemInputDto wishlistAddItemInputDto=new WishlistAddItemInputDto(1L);
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));

        WishlistRemoveItemException wishlistRemoveItemException=assertThrows(WishlistRemoveItemException.class,()->{
            wishlistService.deleteWishlistItem(1L,wishlistAddItemInputDto);
        });

        Assertions.assertEquals("L'id de l'article ne correpond a aucun article",wishlistRemoveItemException.getMessage());
    }

    @Test
    void shouldGetAllArticlesByWishList(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        ArticleEntity articleEntity2=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name2").build();

        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();


        WishlistItemEntity wishlistItem=WishlistItemEntity.builder()
                .article(articleEntity)
                .wishlist(wishlistEntity)
                .build();
        WishlistItemEntity wishlistItem2 =WishlistItemEntity.builder()
                .article(articleEntity2)
                .wishlist(wishlistEntity)
                .build();
        List<WishlistItemEntity> wishlistEntityList= List.of(wishlistItem,wishlistItem2);
        when(wishlistItemRepository.findByWishlist_id(1L)).thenReturn(wishlistEntityList);

        List<AddArticleOutputDto> listAddArticleOutputDto = wishlistService.getAllArticleByWishlistId(1L);

        Assertions.assertEquals(2,listAddArticleOutputDto.size());
    }

    @Test
    void shouldThrownGetAllArticlesByWishListWhenNoIdWishlist(){
        WishlistGetAllArticleException wishlistGetAllArticleException =  assertThrows(WishlistGetAllArticleException.class,()->{
            wishlistService.getAllArticleByWishlistId(null);
        });

        Assertions.assertEquals("L'id de l'article ne doit pas être null",wishlistGetAllArticleException.getMessage());
    }

    @Test
    void shouldThrownGetAllArticlesByWishListWhenNoWishlistIdFound(){
        when(wishlistItemRepository.findByWishlist_id(1L)).thenReturn(null);

        WishlistGetAllArticleException wishlistGetAllArticleException =  assertThrows(WishlistGetAllArticleException.class,()->{
            wishlistService.getAllArticleByWishlistId(1L);
        });

        Assertions.assertEquals("L'id de la liste d'envie n'a aucune liste d'envie ou ne contient aucun article",wishlistGetAllArticleException.getMessage());
    }


















}