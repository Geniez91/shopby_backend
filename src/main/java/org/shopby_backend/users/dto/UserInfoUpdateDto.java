package org.shopby_backend.users.dto;

import jakarta.persistence.Column;

public record UserInfoUpdateDto(String prenom, String nom, String password, String email, String country, String deliveryAddress, String billingAddress) {
}
