package org.shopby_backend.exception;

import org.apache.coyote.BadRequestException;
import org.shopby_backend.exception.article.ArticleCreateException;
import org.shopby_backend.exception.brand.BrandCreateException;
import org.shopby_backend.exception.brand.BrandGetException;
import org.shopby_backend.exception.brand.BrandUpdateException;
import org.shopby_backend.exception.users.UsersCreateException;
import org.shopby_backend.exception.users.UsersUpdateException;
import org.shopby_backend.exception.users.ValidationAccountException;
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
