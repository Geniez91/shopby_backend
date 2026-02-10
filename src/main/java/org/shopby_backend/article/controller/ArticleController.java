package org.shopby_backend.article.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.dto.AddArticleInputDto;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.article.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/article")
public class ArticleController {
private final ArticleService articleService;

@PreAuthorize("hasAnyAuthority('ARTICLE_CREATE')")
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public AddArticleOutputDto addArticle(@Valid @RequestBody AddArticleInputDto addArticleInputDto){
    return articleService.addNewArticle(addArticleInputDto);
}

@PreAuthorize("hasAnyAuthority('ARTICLE_UPDATE')")
@PatchMapping("/{id}")
@ResponseStatus(HttpStatus.OK)
public AddArticleOutputDto updateArticle(@PathVariable Long id,@Valid @RequestBody AddArticleInputDto addArticleInputDto){
    return articleService.updateArticle(id, addArticleInputDto);
}

@PreAuthorize("hasAnyAuthority('ARTICLE_DELETE')")
@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteArticle(@PathVariable Long id){
     articleService.deleteArticle(id);
}

@GetMapping
@ResponseStatus(HttpStatus.OK)
public List<AddArticleOutputDto> getAllArticles(){
    return articleService.getAllArticles();
}

@GetMapping("/{id}")
@ResponseStatus(HttpStatus.OK)
public AddArticleOutputDto getArticleById(@PathVariable Long id){
    return articleService.getArticleById(id);
}

}
