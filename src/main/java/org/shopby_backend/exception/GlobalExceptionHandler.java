package org.shopby_backend.exception;

import org.shopby_backend.exception.article.*;
import org.shopby_backend.exception.articlePhoto.ArticlePhotoUploadException;
import org.shopby_backend.exception.brand.*;
import org.shopby_backend.exception.comment.CommentAlreadyExistsException;
import org.shopby_backend.exception.comment.CommentLikeAlreadyExistsException;
import org.shopby_backend.exception.comment.CommentNotFoundException;
import org.shopby_backend.exception.order.*;
import org.shopby_backend.exception.status.*;
import org.shopby_backend.exception.typeArticle.*;
import org.shopby_backend.exception.users.*;
import org.shopby_backend.exception.wishlist.*;
import org.shopby_backend.tools.ApiErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        /* =========================
       ARTICLE EXCEPTIONS
       ========================= */

    @ExceptionHandler(ArticleAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleArticleAlreadyExistsException(ArticleAlreadyExistsException ex) {
        return buildProblemDetail(HttpStatus.CONFLICT,ex);
    }

    @ExceptionHandler(ArticleCreateException.class)
    public ResponseEntity<ProblemDetail>handleArticleCreate(ArticleCreateException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleArticleNotFound(ArticleNotFoundException ex){
        return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
    }


        /* =========================
       Brand EXCEPTIONS
       ========================= */
        @ExceptionHandler(BrandNotFoundException.class)
        public ResponseEntity<ProblemDetail>handleBrandNotFound(BrandNotFoundException ex){
            return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
        }



        @ExceptionHandler(BrandAlreadyExistsException.class)
        public ResponseEntity<ProblemDetail>handleBrandAlreadyExists(BrandAlreadyExistsException ex){
            return buildProblemDetail(HttpStatus.CONFLICT,ex);
        }



    @ExceptionHandler(BrandCreateException.class)
    public ResponseEntity<ProblemDetail>handleBrandCreate(BrandCreateException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }

            /* =========================
       Order EXCEPTIONS
       ========================= */

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleOrderNotFound(OrderNotFoundException ex){
        return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
    }

    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleOrderItemNotFound(OrderItemNotFoundException ex){
        return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
    }

    @ExceptionHandler(OrderCreateException.class)
    public ResponseEntity<ProblemDetail>handleOrderCreate(OrderCreateException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }

            /* =========================
       Status EXCEPTIONS
       ========================= */

    @ExceptionHandler(StatusAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail>handleStatusAlreadyExists(StatusAlreadyExistsException ex){
        return buildProblemDetail(HttpStatus.CONFLICT,ex);
    }

    @ExceptionHandler(StatusCreateException.class)
    public ResponseEntity<ProblemDetail>handleStatusCreate(StatusCreateException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }

    @ExceptionHandler(StatusNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleStatusNotFound(StatusNotFoundException ex){
        return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
    }

                /* =========================
       Type Article EXCEPTIONS
       ========================= */

    @ExceptionHandler(TypeArticleNotFoundException.class)
    public ResponseEntity<ProblemDetail>handleTypeArticleNotFound(TypeArticleNotFoundException ex){
        return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
    }

    @ExceptionHandler(TypeArticleAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail>handleTypeArticleAlreadyExists(TypeArticleAlreadyExistsException ex){
        return buildProblemDetail(HttpStatus.CONFLICT,ex);
    }

    @ExceptionHandler(TypeArticleAddException.class)
    public ResponseEntity<ProblemDetail>handleTypeArticleAdd(TypeArticleAddException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }
                    /* =========================
       Users EXCEPTIONS
       ========================= */

    @ExceptionHandler(UsersNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUsersNotFound(UsersNotFoundException ex){
        return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
    }

    @ExceptionHandler(UsersAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail>handleUsersAlreadyExists(UsersAlreadyExistsException ex){
        return buildProblemDetail(HttpStatus.CONFLICT,ex);
    }

    @ExceptionHandler(UsersCreateException.class)
    public ResponseEntity<ProblemDetail>handleUsersCreate(UsersCreateException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }

    @ExceptionHandler(NewPasswordException.class)
    public ResponseEntity<ProblemDetail>handleNewPassword(NewPasswordException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }

    @ExceptionHandler(ValidationAccountException.class)
    public ResponseEntity<ProblemDetail>handleValidationAccount(ValidationAccountException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }

    @ExceptionHandler(ValidationNotFoundException.class)
    public ResponseEntity<ProblemDetail>handleValidationNotFound(ValidationNotFoundException ex){
        return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
    }

                    /* =========================
       Photo Article EXCEPTIONS
       ========================= */

    @ExceptionHandler(ArticlePhotoUploadException.class)
    public ResponseEntity<ProblemDetail>handleArticlePhotoUpload(ArticlePhotoUploadException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }

                        /* =========================
       Wishlist EXCEPTIONS
       ========================= */
    @ExceptionHandler(WishlistCreateException.class)
    public ResponseEntity<ProblemDetail>handleWishlistCreate(WishlistCreateException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }

    @ExceptionHandler(WishlistAddItemException.class)
    public ResponseEntity<ProblemDetail>handleWishlistAddItem(WishlistAddItemException ex){
        return buildProblemDetail(HttpStatus.BAD_REQUEST,ex);
    }


    @ExceptionHandler(WishlistItemAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail>handleWishlistItemAlreadyExists(WishlistItemAlreadyExistsException ex){
        return buildProblemDetail(HttpStatus.CONFLICT,ex);
    }

    @ExceptionHandler(WishlistNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleWishlistNotFound(WishlistNotFoundException ex){
        return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
    }




        /* =========================
       Comment EXCEPTIONS
       ========================= */
    @ExceptionHandler(CommentAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail>handleCommentAlreadyExists(CommentAlreadyExistsException ex){
        return buildProblemDetail(HttpStatus.CONFLICT,ex);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ProblemDetail>handleCommentNotFound(CommentNotFoundException ex){
        return buildProblemDetail(HttpStatus.NOT_FOUND,ex);
    }

    @ExceptionHandler(CommentLikeAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail>handleCommentLikeAlreadyExists(CommentLikeAlreadyExistsException ex){
        return buildProblemDetail(HttpStatus.CONFLICT,ex);
    }



    /* =========================
       UTILS
       ========================= */
    private ResponseEntity<ProblemDetail> buildProblemDetail(HttpStatus status, Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        pd.setTitle(ex.getClass().getSimpleName());
        if (ex instanceof ApiErrorCode apiError) {
            pd.setProperty("errorCode", apiError.getErrorCode());
        } else {
            pd.setProperty("errorCode", "INTERNAL_ERROR");
        }
        return ResponseEntity.status(status).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + " : " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                message
        );
        return ResponseEntity.badRequest().body(pd);
    }

@ExceptionHandler(Exception.class)
public ResponseEntity<ProblemDetail> handleException(Exception ex){
    ProblemDetail pd=ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
}


}
