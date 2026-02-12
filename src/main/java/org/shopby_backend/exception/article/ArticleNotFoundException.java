package org.shopby_backend.exception.article;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class ArticleNotFoundException extends BusinessException implements ApiErrorCode {
    public ArticleNotFoundException(Long id) {
        super("Aucun article ne correspond à l'id de l'article "+ id);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ARTICLE_NOT_FOUND;
    }
}
