package org.shopby_backend.brand.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/brand")
public class BrandController {
    private BrandService brandService;

    @PreAuthorize("hasAnyAuthority('BRAND_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BrandOutputDto addBrand(@Valid @RequestBody BrandInputDto brandInputDto){
        return brandService.addBrand(brandInputDto);
    }

    @PreAuthorize("hasAnyAuthority('BRAND_UPDATE')")
    @PatchMapping("/{brandId}")
    @ResponseStatus(HttpStatus.OK)
    public BrandOutputDto updateBrand(@PathVariable Long brandId,@Valid @RequestBody BrandInputDto brandInputDto){
        return brandService.updateBrand(brandId,brandInputDto);
    }

    @PreAuthorize("hasAnyAuthority('BRAND_READ_ALL')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BrandOutputDto> getAllBrands(Pageable pageable){
        return brandService.findAllBrands(pageable);
    }

    @PreAuthorize("hasAnyAuthority('BRAND_READ')")
    @GetMapping("/{brandId}")
    @ResponseStatus(HttpStatus.OK)
    public BrandOutputDto getBrand(@PathVariable Long brandId){
        return brandService.findBrandById(brandId);
    }

    @PreAuthorize("hasAnyAuthority('BRAND_DELETE')")
    @DeleteMapping("/{brandId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBrand(@PathVariable Long brandId){
         brandService.deleteBrand(brandId);
    }


}
