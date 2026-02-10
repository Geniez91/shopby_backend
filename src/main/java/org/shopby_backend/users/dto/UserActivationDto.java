package org.shopby_backend.users.dto;


import jakarta.validation.constraints.NotBlank;

public record UserActivationDto(@NotBlank String code) {
}
