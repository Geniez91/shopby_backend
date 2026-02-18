package org.shopby_backend.exception.comment;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class CommentLikeAlreadyExistsException extends BusinessException implements ApiErrorCode {
    public CommentLikeAlreadyExistsException(Long idComment, Long idUser) {
        super("Le commentaire est deja like avec idComment : "+idComment+" , idUser : "+idUser);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.COMMENT_ALREADY_EXISTS;
    }
}
