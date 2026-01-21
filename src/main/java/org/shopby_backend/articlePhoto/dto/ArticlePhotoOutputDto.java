package org.shopby_backend.articlePhoto.dto;

public record ArticlePhotoOutputDto(Long idPhoto,Long idArticle,String url,String alt,Number position) {
}
