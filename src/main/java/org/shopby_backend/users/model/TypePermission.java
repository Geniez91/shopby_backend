package org.shopby_backend.users.model;

import lombok.Getter;

public enum TypePermission {
    USER_CREATE,
    USER_READ,
    USER_READ_ALL,
    USER_UPDATE,
    USER_DELETE,
    USER_UPDATE_ROLE,

    BRAND_CREATE,
    BRAND_READ,
    BRAND_READ_ALL,
    BRAND_UPDATE,
    BRAND_DELETE,

    TYPE_ARTICLE_CREATE,
    TYPE_ARTICLE_READ,
    TYPE_ARTICLE_READ_ALL,
    TYPE_ARTICLE_UPDATE,
    TYPE_ARTICLE_DELETE,

    ARTICLE_CREATE,
    ARTICLE_READ,
    ARTICLE_READ_ALL,
    ARTICLE_UPDATE,
    ARTICLE_DELETE,

    ARTICLE_PHOTO_UPLOAD
    ;






    @Getter
    private String libelle;
}
