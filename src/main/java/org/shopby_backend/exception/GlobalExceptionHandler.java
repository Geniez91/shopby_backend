package org.shopby_backend.exception;

import org.apache.coyote.BadRequestException;
import org.shopby_backend.exception.article.ArticleCreateException;
import org.shopby_backend.exception.article.ArticleDeleteException;
import org.shopby_backend.exception.article.ArticleGetException;
import org.shopby_backend.exception.article.ArticleUpdateException;
import org.shopby_backend.exception.articlePhoto.ArticlePhotoUpload;
import org.shopby_backend.exception.brand.BrandCreateException;
import org.shopby_backend.exception.brand.BrandDeleteException;
import org.shopby_backend.exception.brand.BrandGetException;
import org.shopby_backend.exception.brand.BrandUpdateException;
import org.shopby_backend.exception.typeArticle.TypeArticleAddException;
import org.shopby_backend.exception.typeArticle.TypeArticleDeleteException;
import org.shopby_backend.exception.typeArticle.TypeArticleGetException;
import org.shopby_backend.exception.typeArticle.TypeArticleUpdateException;
import org.shopby_backend.exception.users.UsersCreateException;
import org.shopby_backend.exception.users.UsersUpdateException;
import org.shopby_backend.exception.users.ValidationAccountException;
import org.shopby_backend.exception.wishlist.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(ValidationAccountException.class)
public ResponseEntity<ProblemDetail>catchValidationAccount(UsersCreateException ex){
    ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
}

@ExceptionHandler(UsersCreateException.class)
public ResponseEntity<ProblemDetail>catchBookCreation(UsersCreateException ex){
  ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
}

@ExceptionHandler(UsersUpdateException.class)
    public ResponseEntity<ProblemDetail>catchUsersUpdate(UsersUpdateException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }


    @ExceptionHandler(ArticleCreateException.class)
    public ResponseEntity<ProblemDetail>catchArticleCreateException(ArticleCreateException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(BrandCreateException.class)
    public ResponseEntity<ProblemDetail>catchBrandCreateException(BrandCreateException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }


    @ExceptionHandler(BrandUpdateException.class)
    public ResponseEntity<ProblemDetail>catchBrandUpdateException(BrandUpdateException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }
    @ExceptionHandler(BrandGetException.class)
    public ResponseEntity<ProblemDetail>catchBrandGetException(BrandGetException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }
    @ExceptionHandler(BrandDeleteException.class)
    public ResponseEntity<ProblemDetail>catchBrandDeleteException(BrandGetException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(TypeArticleAddException.class)
    public ResponseEntity<ProblemDetail>catchTypeArticleAddException(TypeArticleAddException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(TypeArticleUpdateException.class)
    public ResponseEntity<ProblemDetail>catchTypeArticleUpdateException(TypeArticleUpdateException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(TypeArticleDeleteException.class)
    public ResponseEntity<ProblemDetail>catchTypeArticleDeleteException(TypeArticleDeleteException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(TypeArticleGetException.class)
    public ResponseEntity<ProblemDetail>catchTypeArticleDeleteException(TypeArticleGetException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(ArticleUpdateException.class)
    public ResponseEntity<ProblemDetail>catchArticleUpdateException(ArticleUpdateException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(ArticleDeleteException.class)
    public ResponseEntity<ProblemDetail>catchArticleDeleteException(ArticleDeleteException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(ArticleGetException.class)
    public ResponseEntity<ProblemDetail>catchArticleGetException(ArticleGetException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(ArticlePhotoUpload.class)
    public ResponseEntity<ProblemDetail>catchArticlePhotoUploadException(ArticlePhotoUpload ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }
    @ExceptionHandler(WishlistCreateException.class)
    public ResponseEntity<ProblemDetail>catchWishlistCreateException(WishlistCreateException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(WishlistUpdateException.class)
    public ResponseEntity<ProblemDetail>catchWishlistUpdateException(WishlistUpdateException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }
    @ExceptionHandler(WishlistGetException.class)
    public ResponseEntity<ProblemDetail>catchWishlistGetException(WishlistGetException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }
    @ExceptionHandler(WishlistGetAllByUserIdException.class)
    public ResponseEntity<ProblemDetail>catchWishlistGetAllByUserIdException(WishlistGetAllByUserIdException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }
    @ExceptionHandler(WishlistAddItemException.class)
    public ResponseEntity<ProblemDetail>catchWishlistAddItemException(WishlistAddItemException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }
    @ExceptionHandler(WishlistRemoveItemException.class)
    public ResponseEntity<ProblemDetail>catchWishlistRemoveItemException(WishlistRemoveItemException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

    @ExceptionHandler(WishlistGetAllArticleException.class)
    public ResponseEntity<ProblemDetail>catchWishlistGetArticleException(WishlistGetAllArticleException ex){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }





    @ExceptionHandler(BadRequestException.class)
public ResponseEntity<ProblemDetail> handleBadRequestException(BadRequestException ex){
    ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
}

@ExceptionHandler(Exception.class)
public ResponseEntity<ProblemDetail> handleException(Exception ex){
    ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
}


}
