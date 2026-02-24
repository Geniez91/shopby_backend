package org.shopby_backend.typeArticle.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.service.TypeArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/type_article")
public class TypeArticleController {
    private TypeArticleService typeArticleService;

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeArticleOutputDto addTypeArticle(@Valid @RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.addTypeArticle(typeArticleDto);
    }

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_UPDATE')")
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public TypeArticleOutputDto updateTypeArticle(@PathVariable Long id, @Valid @RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.updateTypeArticle(id, typeArticleDto);
    }

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_DELETE')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTypeArticle(@PathVariable Long id) {
         typeArticleService.deleteTypeArticle(id);
    }

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_READ_ALL')")
    @GetMapping
    public Page<TypeArticleOutputDto> getAllTypeArticle(Pageable pageable) {
        return typeArticleService.getAllTypeArticle(pageable);
    }

    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_READ')")
    @GetMapping("/{id}")
    public TypeArticleOutputDto getTypeArticleById(@PathVariable Long id) {
        return typeArticleService.getTypeArticleById(id);
    }


}
