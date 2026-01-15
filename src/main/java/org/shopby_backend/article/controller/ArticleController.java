package org.shopby_backend.article.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.service.ArticleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ArticleController {
private final ArticleService articleService;

@PostMapping("/article")
public AddArticleOutputDto addArticle(AddArticleInputDto addArticleInputDto){
   return articleService.addNewArticle(addArticleInputDto);
}

}
