package org.shopby_backend.users.model;

import lombok.Getter;

public enum TypePermission {
    ADMIN_CREATE,
    ADMIN_READ,
    ADMIN_UPDATE,
    ADMIN_DELETE,

    MANAGER_CREATE,
    MANAGER_READ,
    MANAGER_UPDATE,
    MANAGER_DELETE,

    USER_CREATE_ACCOUNT,
    USER_CREATE_COMMENT;

    @Getter
    private String libelle;
}
