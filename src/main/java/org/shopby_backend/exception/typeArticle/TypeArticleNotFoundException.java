package org.shopby_backend.exception.typeArticle;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.exception.status.StatusNotFoundException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class TypeArticleNotFoundException extends BusinessException implements ApiErrorCode {
    public TypeArticleNotFoundException(String message) {
        super(message);
    }

    public static TypeArticleNotFoundException byParentId(Long parentId){
        return new TypeArticleNotFoundException("Le parent est introuvable avec l'id "+parentId);
    }

    public static TypeArticleNotFoundException byId(Long id){
        return new TypeArticleNotFoundException("Aucun type article n'existe avec l'id "+id);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.TYPE_ARTICLE_NOT_FOUND;
    }
}
