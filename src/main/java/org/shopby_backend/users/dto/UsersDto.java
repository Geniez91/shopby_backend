package org.shopby_backend.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represente un utilisateur affiché coté client")
public record UsersDto(Long id, String prenom, String nom, String password, String email, String country,String deliveryAddress, String billingAddress, Long version) { }
