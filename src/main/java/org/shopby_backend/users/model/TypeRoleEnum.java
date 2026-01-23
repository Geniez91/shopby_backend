package org.shopby_backend.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum TypeRoleEnum {
    USER(
            Set.of(TypePermission.USER_CREATE,TypePermission.USER_READ,TypePermission.USER_UPDATE,TypePermission.ARTICLE_READ,TypePermission.ARTICLE_READ_ALL)
    ),
    ADMIN(
            Set.of(
                    TypePermission.USER_CREATE,
                    TypePermission.USER_READ,
                    TypePermission.USER_UPDATE,
                    TypePermission.USER_DELETE,
                    TypePermission.USER_UPDATE_ROLE,

                    TypePermission.ARTICLE_CREATE,
                    TypePermission.ARTICLE_DELETE,
                    TypePermission.ARTICLE_UPDATE,
                    TypePermission.ARTICLE_READ,
                    TypePermission.ARTICLE_READ_ALL,

                    TypePermission.BRAND_CREATE,
                    TypePermission.BRAND_READ,
                    TypePermission.BRAND_READ_ALL,
                    TypePermission.BRAND_UPDATE,
                    TypePermission.BRAND_DELETE,

                    TypePermission.TYPE_ARTICLE_CREATE,
                    TypePermission.TYPE_ARTICLE_READ,
                    TypePermission.TYPE_ARTICLE_READ_ALL,
                    TypePermission.TYPE_ARTICLE_UPDATE,
                    TypePermission.TYPE_ARTICLE_DELETE,

                    TypePermission.ARTICLE_PHOTO_UPLOAD
            )
    ),
    MANAGER(
            Set.of( TypePermission.USER_CREATE,
                    TypePermission.USER_READ,
                    TypePermission.USER_UPDATE,

                    TypePermission.ARTICLE_CREATE,
                    TypePermission.ARTICLE_DELETE,
                    TypePermission.ARTICLE_UPDATE,
                    TypePermission.ARTICLE_READ,
                    TypePermission.ARTICLE_READ_ALL,

                    TypePermission.BRAND_CREATE,
                    TypePermission.BRAND_READ,
                    TypePermission.BRAND_READ_ALL,
                    TypePermission.BRAND_UPDATE,
                    TypePermission.BRAND_DELETE,

                    TypePermission.TYPE_ARTICLE_CREATE,
                    TypePermission.TYPE_ARTICLE_READ,
                    TypePermission.TYPE_ARTICLE_READ_ALL,
                    TypePermission.TYPE_ARTICLE_UPDATE,
                    TypePermission.TYPE_ARTICLE_DELETE,

                    TypePermission.ARTICLE_PHOTO_UPLOAD)
    );

    @Getter
    Set<TypePermission> permissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Crée une liste mutable à partir du stream
        List<SimpleGrantedAuthority> grantedAuthorities = this.getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toCollection(ArrayList::new));

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return grantedAuthorities;
    }

}
