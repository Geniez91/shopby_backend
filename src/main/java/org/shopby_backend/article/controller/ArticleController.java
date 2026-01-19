package org.shopby_backend.article.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class ArticleController {
private final ArticleService articleService;

@PostMapping("/article")
public AddArticleOutputDto addArticle(AddArticleInputDto addArticleInputDto){
   return articleService.addNewArticle(addArticleInputDto);
}

@PatchMapping("/article/{id}")
public AddArticleOutputDto updateArticle(@PathVariable Long id,AddArticleInputDto addArticleInputDto){
    return articleService.updateArticle(id, addArticleInputDto);
}

@GetMapping("/article")
public List<AddArticleOutputDto> getAllArticles(){
    return articleService.getAllArticles();
}

@GetMapping("/article/{id}")
public AddArticleOutputDto getArticleById(@PathVariable Long id){
    return articleService.getArticleById(id);
}

}
