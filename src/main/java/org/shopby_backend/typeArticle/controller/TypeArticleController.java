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

    @PostMapping
    public TypeArticleOutputDto addTypeArticle(@RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.addTypeArticle(typeArticleDto);
    }

    @PatchMapping
    public TypeArticleOutputDto updateTypeArticle(@PathVariable Long id, @RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.updateTypeArticle(id, typeArticleDto);
    }

    @DeleteMapping
    public TypeArticleOutputDto deleteTypeArticle(@PathVariable Long id) {
        return typeArticleService.deleteTypeArticle(id);
    }

    @GetMapping
    public List<TypeArticleOutputDto> getAllTypeArticle() {
        return typeArticleService.getAllTypeArticle();
    }

    @GetMapping
    public TypeArticleOutputDto getTypeArticleById(@PathVariable Long id) {
        return typeArticleService.getTypeArticleById(id);
    }


}
