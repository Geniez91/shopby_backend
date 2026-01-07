package org.shopby_backend.users.dto;

import org.shopby_backend.users.model.TypeRoleEnum;

public record UserUpdateRoleDto(String email, TypeRoleEnum role) {
}
