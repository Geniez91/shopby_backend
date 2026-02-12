package org.shopby_backend.exception.typeArticle;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class TypeArticleAlreadyExistsException extends BusinessException implements ApiErrorCode {
    public TypeArticleAlreadyExistsException(String libelle) {
        super("Le type d'article existe deja avec le libelle "+libelle);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.TYPE_ARTICLE_ALREADY_EXISTS;
    }
}
