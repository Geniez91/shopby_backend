package org.shopby_backend.typeArticle.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.service.TypeArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class TypeArticleController {
    private TypeArticleService typeArticleService;

    @PostMapping("/type_article")
    public TypeArticleOutputDto addTypeArticle(@RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.addTypeArticle(typeArticleDto);
    }

    @PatchMapping("/type_article")
    public TypeArticleOutputDto updateTypeArticle(@PathVariable Long id, @RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.updateTypeArticle(id, typeArticleDto);
    }

    @DeleteMapping("/type_article/{id}")
    public TypeArticleOutputDto deleteTypeArticle(@PathVariable Long id) {
        return typeArticleService.deleteTypeArticle(id);
    }

    @GetMapping("/type_article")
    public List<TypeArticleOutputDto> getAllTypeArticle() {
        return typeArticleService.getAllTypeArticle();
    }

    @GetMapping("/type_article/{id}")
    public TypeArticleOutputDto getTypeArticleById(@PathVariable Long id) {
        return typeArticleService.getTypeArticleById(id);
    }


}
