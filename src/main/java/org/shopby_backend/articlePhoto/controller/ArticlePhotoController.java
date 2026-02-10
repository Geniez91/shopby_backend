package org.shopby_backend.articlePhoto.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.articlePhoto.dto.ArticlePhotoOutputDto;
import org.shopby_backend.articlePhoto.service.ArticlePhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
public class ArticlePhotoController {
    private ArticlePhotoService articlePhotoService;

    @PreAuthorize("hasAnyAuthority('ARTICLE_PHOTO_UPLOAD')")
    @PostMapping(value = "/article/{idArticle}/photos",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<ArticlePhotoOutputDto> uploadPhotos(@PathVariable Long idArticle, @RequestPart("files") List<MultipartFile> files){
        return  articlePhotoService.uploadPhotos(idArticle,files);
    }
}
