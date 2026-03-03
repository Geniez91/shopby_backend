package org.shopby_backend.wishlist.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.mapper.ArticleMapper;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.service.ArticleService;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.exception.article.ArticleNotFoundException;
import org.shopby_backend.exception.users.UsersNotFoundException;
import org.shopby_backend.exception.wishlist.*;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.service.UsersService;
import org.shopby_backend.wishlist.dto.*;
import org.shopby_backend.wishlist.mapper.WishlistItemMapper;
import org.shopby_backend.wishlist.mapper.WishlistMapper;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.shopby_backend.wishlist.model.WishlistItemEntity;
import org.shopby_backend.wishlist.persistence.WishlistItemRepository;
import org.shopby_backend.wishlist.persistence.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {
    @Mock
    ArticleService articleService;

    @Mock
    WishlistRepository wishlistRepository;

    @Mock
    WishlistItemRepository wishlistItemRepository;

    @Mock
    UsersService usersService;

    WishlistService wishlistService;

    WishlistMapper wishlistMapper=new WishlistMapper();
    WishlistItemMapper wishlistItemMapper=new WishlistItemMapper();
    ArticleMapper articleMapper=new ArticleMapper();

    @BeforeEach
    void setUp() {
        wishlistService=new WishlistService(wishlistRepository,wishlistItemRepository,wishlistMapper,wishlistItemMapper,usersService,articleService,articleMapper);
    }


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
        when(usersService.findUsersOrThrow(1L)).thenReturn(user);
        when(wishlistRepository.save(any(WishlistEntity.class))).thenReturn(wishlistEntity);

        WishlistOutputDto wishlistOutputDto = wishlistService.addWishList(wishlistInputDto);

        Assertions.assertEquals("liste d'envie",wishlistOutputDto.name());
    }

    @Test
    void shouldThrowAnExceptionAddNewWishlistWhenUserNotFound() {
        WishlistInputDto wishlistInputDto=new WishlistInputDto(1L,"liste d'envie","description");
        when(usersService.findUsersOrThrow(1L)).thenThrow(new UsersNotFoundException("L'utilisateur n'existe pas avec l'id user :1"));

        UsersNotFoundException usersNotFoundException=assertThrows(UsersNotFoundException.class,()->{
            wishlistService.addWishList(wishlistInputDto);
        });

        Assertions.assertEquals("L'utilisateur n'existe pas avec l'id user :1",usersNotFoundException.getMessage());
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

        WishlistNotFoundException wishlistNotFoundException=assertThrows(WishlistNotFoundException.class,()->{
            wishlistService.updateWishlist(1,wishlistInputDto);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste avec l'id 1",wishlistNotFoundException.getMessage());
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

        wishlistService.deleteWishlist(1);

        verify(wishlistRepository).delete(wishlistEntity);
    }

    @Test
    void shouldThrownAnExceptionDeleteWishlistWhenWishlistNotFound(){
        when(wishlistRepository.findById(1)).thenReturn(Optional.empty());

        WishlistNotFoundException wishlistNotFoundException=assertThrows(WishlistNotFoundException.class,()->{
            wishlistService.deleteWishlist(1);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste avec l'id 1",wishlistNotFoundException.getMessage());
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

        WishlistNotFoundException wishlistNotFoundException=assertThrows(WishlistNotFoundException.class,()->{
            wishlistService.getWishlist(1);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste avec l'id 1",wishlistNotFoundException.getMessage());
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
        Page<WishlistEntity> page=new PageImpl<>(wishlistEntityList);
        WishlistFilter filter = new WishlistFilter("name",null);
        Pageable pageable=PageRequest.of(0, 10);
        when(usersService.findUsersOrThrow(1L)).thenReturn(user);
        when(wishlistRepository.findByUserId(any(Specification.class),eq(user.getId()),any(Pageable.class))).thenReturn(page);

        Page<WishlistOutputDto> wishlistOutputDtos = wishlistService.getAllWishListByUserId(filter,wishListGetAllByIdDto,pageable);

        Assertions.assertEquals("name",wishlistOutputDtos.getContent().get(0).name());
        Assertions.assertEquals("description",wishlistOutputDtos.getContent().get(0).description());
        Assertions.assertEquals("name",wishlistOutputDtos.getContent().get(1).name());
        Assertions.assertEquals("description",wishlistOutputDtos.getContent().get(1).description());
    }

    @Test
    void shouldThrownAnExceptionGetAllWishlistWhenNoUserFound(){
        WishListGetAllByIdDto wishListGetAllByIdDto = new WishListGetAllByIdDto(1L);
        WishlistFilter filter = new WishlistFilter("name",null);
        Pageable pageable=PageRequest.of(0, 10);
        when(usersService.findUsersOrThrow(1L)).thenThrow(new UsersNotFoundException("L'utilisateur n'existe pas avec l'id user :1"));

        UsersNotFoundException usersNotFoundException=assertThrows(UsersNotFoundException.class,()->{
            wishlistService.getAllWishListByUserId(filter,wishListGetAllByIdDto,pageable);
        });

        Assertions.assertEquals("L'utilisateur n'existe pas avec l'id user :1", usersNotFoundException.getMessage());
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
        when(articleService.findArticleOrThrow(wishlistAddItemInputDto.idArticle())).thenReturn(articleEntity);
        when(wishlistItemRepository.existsByWishlist_IdWishlistAndArticle_IdArticle(wishlistEntity.getIdWishlist(),articleEntity.getIdArticle())).thenReturn(false);
        when(wishlistItemRepository.save(any(WishlistItemEntity.class))).thenReturn(wishlistItem);

        WishlistAddItemOutputDto wishlistAddItemOutputDto= wishlistService.addWishListItem(1L,wishlistAddItemInputDto);

        Assertions.assertEquals("name",wishlistAddItemOutputDto.name());
        Assertions.assertEquals("description",wishlistAddItemOutputDto.description());

    }

    @Test
    void shouldThrownAnExceptionAddWishlistItemWhenNoArticleId(){
        WishlistAddItemInputDto wishlistAddItemInputDto= new WishlistAddItemInputDto(null);

        WishlistNotFoundException wishlistNotFoundException=assertThrows(WishlistNotFoundException.class,()->{
            wishlistService.addWishListItem(1L,wishlistAddItemInputDto);
        });
        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste avec l'id 1", wishlistNotFoundException.getMessage());
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

        WishlistNotFoundException wishlistNotFoundException=assertThrows(WishlistNotFoundException.class,()->{
            wishlistService.addWishListItem(1L,wishlistAddItemInputDto);
        });
        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste avec l'id 1", wishlistNotFoundException.getMessage());
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
        when(articleService.findArticleOrThrow(wishlistAddItemInputDto.idArticle())).thenThrow(new ArticleNotFoundException(1L));

        ArticleNotFoundException articleNotFoundException=assertThrows(ArticleNotFoundException.class,()->{
            wishlistService.addWishListItem(1L,wishlistAddItemInputDto);
        });
        Assertions.assertEquals("Aucun article ne correspond à l'id de l'article 1", articleNotFoundException.getMessage());
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
        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));
        when(articleService.findArticleOrThrow(wishlistAddItemInputDto.idArticle())).thenReturn(articleEntity);
        when(wishlistItemRepository.existsByWishlist_IdWishlistAndArticle_IdArticle(wishlistEntity.getIdWishlist(),articleEntity.getIdArticle())).thenReturn(true);

        WishlistItemAlreadyExistsException wishlistItemAlreadyExistsException=assertThrows(WishlistItemAlreadyExistsException.class,()->{
            wishlistService.addWishListItem(1L,wishlistAddItemInputDto);
        });

        Assertions.assertEquals("L'article est deja dans votre liste d'envie avec l'id wishlist : 1et l'id article : 1", wishlistItemAlreadyExistsException.getMessage());
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
        when(articleService.findArticleOrThrow(wishlistAddItemInputDto.idArticle())).thenReturn(articleEntity);
        when(wishlistItemRepository.findByWishlist_idWishlistAndArticle_idArticle(1L,wishlistAddItemInputDto.idArticle())).thenReturn(Optional.ofNullable(wishlistItem));

       wishlistService.deleteWishlistItem(1L,wishlistAddItemInputDto);

       verify(wishlistItemRepository).delete(wishlistItem);
    }



    @Test
    void shouldThrownAnExceptionDeleteArticleWhenIdArticleIsNull(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        WishlistAddItemInputDto wishlistAddItemInputDto=new WishlistAddItemInputDto(null);
        WishlistEntity wishlistEntity=WishlistEntity.builder()
                .idWishlist(1L)
                .user(user)
                .name("name")
                .description("description")
                .build();
        when(wishlistRepository.findById(1)).thenReturn(Optional.of(wishlistEntity));

        WishlistRemoveItemException wishlistRemoveItemException = assertThrows(WishlistRemoveItemException.class,()->{
            wishlistService.deleteWishlistItem(1L,wishlistAddItemInputDto);
        });

        Assertions.assertEquals("L'id de liste d'envie et de l'article ne correpond a aucun article d'une liste d'envie avec idWishlist :1 et idArticle : null",wishlistRemoveItemException.getMessage());
    }

    @Test
    void shouldThrownAnExceptionDeleteArticleWhenWishlistNotFound(){
        WishlistAddItemInputDto wishlistAddItemInputDto=new WishlistAddItemInputDto(1L);
        when(wishlistRepository.findById(1)).thenReturn(Optional.empty());

        WishlistNotFoundException wishlistNotFoundException=assertThrows(WishlistNotFoundException.class,()->{
            wishlistService.deleteWishlistItem(1L,wishlistAddItemInputDto);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste avec l'id 1",wishlistNotFoundException.getMessage());
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
        when(articleService.findArticleOrThrow(wishlistAddItemInputDto.idArticle())).thenThrow(new ArticleNotFoundException(1L));

        ArticleNotFoundException articleNotFoundException=assertThrows(ArticleNotFoundException.class,()->{
            wishlistService.deleteWishlistItem(1L,wishlistAddItemInputDto);
        });

        Assertions.assertEquals("Aucun article ne correspond à l'id de l'article 1",articleNotFoundException.getMessage());
    }

    @Test
    void shouldGetAllArticlesByWishList(){
        UsersEntity user = UsersEntity.builder().id(1L).nom("Weltmann").prenom("Jeremy").email("jeremy@example.com").password("test123").country("France").build();
        BrandEntity brandEntity= BrandEntity.builder().idBrand(1L).libelle("DC").build();
        TypeArticleEntity typeArticleEntity=TypeArticleEntity.builder().idTypeArticle(1L).libelle("Comics").build();
        ArticleEntity articleEntity=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name").build();
        ArticleEntity articleEntity2=ArticleEntity.builder().idArticle(1L).price(BigDecimal.valueOf(2)).typeArticle(typeArticleEntity).brand(brandEntity).description("desc").name("name2").build();
        Pageable pageable=PageRequest.of(0, 10);

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
        Page<WishlistItemEntity> page= new PageImpl<WishlistItemEntity>(wishlistEntityList);

        when(wishlistItemRepository.findByWishlist_idWishlist(1L,pageable)).thenReturn(Optional.of(page));

        Page<AddArticleOutputDto> listAddArticleOutputDto = wishlistService.getAllArticleByWishlistId(1L,pageable);

        Assertions.assertEquals(2,listAddArticleOutputDto.getContent().size());
    }


    @Test
    void shouldThrownGetAllArticlesByWishListWhenNoWishlistIdFound(){
        Pageable pageable=PageRequest.of(0, 10);
        when(wishlistItemRepository.findByWishlist_idWishlist(1L,any(Pageable.class))).thenReturn(Optional.empty());

        WishlistNotFoundException wishlistNotFoundException =  assertThrows(WishlistNotFoundException.class,()->{
            wishlistService.getAllArticleByWishlistId(1L,pageable);
        });

        Assertions.assertEquals("L'id de la liste d'envie ne correspond a aucun liste avec l'id 1",wishlistNotFoundException.getMessage());
    }


















}