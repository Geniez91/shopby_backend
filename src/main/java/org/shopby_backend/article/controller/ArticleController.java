package org.shopby_backend.article.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.service.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class ArticleController {
private final ArticleService articleService;

@PreAuthorize("hasAnyAuthority('ARTICLE_CREATE')")
@PostMapping("/article")
public AddArticleOutputDto addArticle(@RequestBody AddArticleInputDto addArticleInputDto){
   return articleService.addNewArticle(addArticleInputDto);
}

@PreAuthorize("hasAnyAuthority('ARTICLE_UPDATE')")
@PatchMapping("/article/{id}")
public AddArticleOutputDto updateArticle(@PathVariable Long id,@RequestBody AddArticleInputDto addArticleInputDto){
    return articleService.updateArticle(id, addArticleInputDto);
}

@PreAuthorize("hasAnyAuthority('ARTICLE_DELETE')")
@DeleteMapping("/article/{id}")
public AddArticleOutputDto deleteArticle(@PathVariable Long id){
    return articleService.deleteArticle(id);
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
