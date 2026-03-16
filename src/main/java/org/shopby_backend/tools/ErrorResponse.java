package org.shopby_backend.tools;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Structure standard des erreurs API")
public record ErrorResponse(

        @Schema(description = "Code HTTP", example = "404")
        int status,

        @Schema(description = "Message d'erreur", example = "Ressource non trouvé")
        String message,

        @Schema(description = "Heure de l'erreur", example = "2026-03-16T15:30:00")
        String timestamp
) {}