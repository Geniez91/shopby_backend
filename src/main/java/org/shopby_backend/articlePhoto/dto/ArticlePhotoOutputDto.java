package org.shopby_backend.articlePhoto.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represente la photo ajouté à l'article affiché au client")
public record ArticlePhotoOutputDto(Long idPhoto,Long idArticle,String url,String alt,Number position) {
}
