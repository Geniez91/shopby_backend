package org.shopby_backend.exception.comment;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class CommentAlreadyExistsException extends BusinessException implements ApiErrorCode {
    public CommentAlreadyExistsException(Long idArticle, Long idUser) {
        super("Le commentaire existe deja avec idArticle : "+idArticle+" , idUser : "+idUser);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.COMMENT_ALREADY_EXISTS;
    }
}
