package org.shopby_backend.exception.article;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class ArticleAlreadyExistsException extends BusinessException implements ApiErrorCode {
    public ArticleAlreadyExistsException(String articleName) {
        super("L'article existe deja : articleName :"+ articleName);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ARTICLE_ALREADY_EXISTS;
    }
}
