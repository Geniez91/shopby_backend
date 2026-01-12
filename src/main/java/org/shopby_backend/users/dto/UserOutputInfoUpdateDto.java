package org.shopby_backend.users.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserOutputInfoUpdateDto {
     Long id;

     String prenom;

     String nom;

     String password;

     String email;

     String country;

     String deliveryAddress;

     String billingAddress;
}