package org.shopby_backend.brand.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.service.BrandService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class BrandController {
    private BrandService brandService;

    @PreAuthorize("hasAnyAuthority('BRAND_CREATE')")
    @PostMapping("/brand")
    public BrandOutputDto addBrand(@RequestBody BrandInputDto brandInputDto){
        return brandService.addBrand(brandInputDto);
    }

    @PreAuthorize("hasAnyAuthority('BRAND_UPDATE')")
    @PatchMapping("/brand/{brandId}")
    public BrandOutputDto updateBrand(@PathVariable Long brandId,@RequestBody BrandInputDto brandInputDto){
        return brandService.updateBrand(brandId,brandInputDto);
    }

    @PreAuthorize("hasAnyAuthority('BRAND_READ_ALL')")
    @GetMapping("/brand")
    public List<BrandOutputDto> getAllBrands(){
        return brandService.findAllBrands();
    }

    @PreAuthorize("hasAnyAuthority('BRAND_READ')")
    @GetMapping("/brand/{brandId}")
    public BrandOutputDto getBrand(@PathVariable Long brandId){
        return brandService.findBrandById(brandId);
    }

    @PreAuthorize("hasAnyAuthority('BRAND_DELETE')")
    @DeleteMapping("/brand/{brandId}")
    public BrandOutputDto deleteBrand(@PathVariable Long brandId){
        return brandService.deleteBrand(brandId);
    }


}
