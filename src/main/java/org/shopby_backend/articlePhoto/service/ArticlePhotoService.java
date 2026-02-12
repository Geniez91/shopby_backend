package org.shopby_backend.articlePhoto.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.articlePhoto.dto.ArticlePhotoOutputDto;
import org.shopby_backend.articlePhoto.model.ArticlePhotoEntity;
import org.shopby_backend.articlePhoto.persistence.ArticlePhotoRepository;
import org.shopby_backend.exception.article.ArticleNotFoundException;
import org.shopby_backend.exception.articlePhoto.ArticlePhotoUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ArticlePhotoService {
    private ArticleRepository articleRepository;
    private ArticlePhotoRepository articlePhotoRepository;
    private LocalStorageService localStorageService;

    public List<ArticlePhotoOutputDto> uploadPhotos(Long articleId,List<MultipartFile> files){
        ArticleEntity articleEntity=articleRepository.findById(articleId).orElseThrow(()->new ArticleNotFoundException(articleId));
        if(files==null|| files.isEmpty()){
            throw new ArticlePhotoUploadException("Aucun fichiers n'a été transmis");
        }
        int startPosition=articlePhotoRepository.findMaxPositionByArticleId(articleId).orElse(0)+1;

        List<ArticlePhotoEntity> listArticlePhoto=new ArrayList<>();
        for(int i=0;i<files.size();i++){
            String url= localStorageService.upload(files.get(i));
            ArticlePhotoEntity articlePhotoEntity=ArticlePhotoEntity.builder()
                    .article(articleEntity)
                    .url(url)
                    .position(startPosition+i)
                    .build();
            listArticlePhoto.add(articlePhotoEntity);
            articlePhotoRepository.save(articlePhotoEntity);
        }
        return listArticlePhoto.stream().map(articlePhoto->new ArticlePhotoOutputDto(articlePhoto.getIdPhoto(),articlePhoto.getArticle().getIdArticle(),articlePhoto.getUrl(),articlePhoto.getAlt(),articlePhoto.getPosition()))
                .toList();
    }
}
