package org.shopby_backend.typeArticle.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.service.TypeArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class TypeArticleController {
    private TypeArticleService typeArticleService;

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_CREATE')")
    @PostMapping("/type_article")
    public TypeArticleOutputDto addTypeArticle(@RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.addTypeArticle(typeArticleDto);
    }

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_UPDATE')")
    @PatchMapping("/type_article")
    public TypeArticleOutputDto updateTypeArticle(@PathVariable Long id, @RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.updateTypeArticle(id, typeArticleDto);
    }

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_DELETE')")
    @DeleteMapping("/type_article/{id}")
    public TypeArticleOutputDto deleteTypeArticle(@PathVariable Long id) {
        return typeArticleService.deleteTypeArticle(id);
    }

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_READ_ALL')")
    @GetMapping("/type_article")
    public List<TypeArticleOutputDto> getAllTypeArticle() {
        return typeArticleService.getAllTypeArticle();
    }

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_READ')")
    @GetMapping("/type_article/{id}")
    public TypeArticleOutputDto getTypeArticleById(@PathVariable Long id) {
        return typeArticleService.getTypeArticleById(id);
    }


}
