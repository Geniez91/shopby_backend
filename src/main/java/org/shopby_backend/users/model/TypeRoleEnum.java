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
            Set.of(TypePermission.USER_CREATE_ACCOUNT,TypePermission.USER_CREATE_COMMENT)
    ),
    ADMIN(
            Set.of(TypePermission.USER_CREATE_COMMENT,
                    TypePermission.USER_CREATE_ACCOUNT,
                    TypePermission.MANAGER_CREATE,
                    TypePermission.MANAGER_UPDATE,
                    TypePermission.MANAGER_READ,
                    TypePermission.MANAGER_DELETE,
                    TypePermission.ADMIN_CREATE,
                    TypePermission.ADMIN_DELETE,
                    TypePermission.ADMIN_READ,
                    TypePermission.ADMIN_UPDATE
            )
    ),
    MANAGER(
            Set.of(TypePermission.USER_CREATE_COMMENT,
                    TypePermission.USER_CREATE_ACCOUNT,
                    TypePermission.MANAGER_CREATE,
                    TypePermission.MANAGER_UPDATE,
                    TypePermission.MANAGER_READ,
                    TypePermission.MANAGER_DELETE)
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
