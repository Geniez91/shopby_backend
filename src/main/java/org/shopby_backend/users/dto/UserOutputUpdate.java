package org.shopby_backend.users.dto;

public record UserOutputUpdate(String prenom,String nom,String password,String email,String country,String deliveryAddress, String billingAddress) {
}
