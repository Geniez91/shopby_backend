package org.shopby_backend.exception.comment;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class CommentLikeNotFoundException extends BusinessException implements ApiErrorCode {
    public CommentLikeNotFoundException(Long idComment) {
        super("Aucun like sur commentaire ne correspond à l'id du commentaire : "+idComment );
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.COMMENT_LIKE_NOT_FOUND;
    }
}
