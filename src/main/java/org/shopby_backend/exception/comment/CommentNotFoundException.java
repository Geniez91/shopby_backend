package org.shopby_backend.exception.comment;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class CommentNotFoundException extends BusinessException implements ApiErrorCode {
    public CommentNotFoundException(Long idComment) {
        super("Aucune commentaire ne correspond à l'id du commentaire : "+idComment );
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.COMMENT_NOT_FOUND;
    }
}
