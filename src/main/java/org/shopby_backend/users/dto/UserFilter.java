package org.shopby_backend.users.dto;

public record UserFilter(String prenom,String nom, Long roleId,String country) {
}
