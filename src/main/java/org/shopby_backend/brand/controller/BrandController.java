package org.shopby_backend.brand.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.service.BrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class BrandController {
    private BrandService brandService;

    @PostMapping("/brand")
    public BrandOutputDto addBrand(@RequestBody BrandInputDto brandInputDto){
        return brandService.addBrand(brandInputDto);
    }

    @PatchMapping("/brand/{brandId}")
    public BrandOutputDto updateBrand(@PathVariable Long brandId,@RequestBody BrandInputDto brandInputDto){
        return brandService.updateBrand(brandId,brandInputDto);
    }

    @GetMapping("/brand")
    public List<BrandOutputDto> getAllBrands(){
        return brandService.findAllBrands();
    }

    @GetMapping("/brand/{brandId}")
    public BrandOutputDto getBrand(@PathVariable Long brandId){
        return brandService.findBrandById(brandId);
    }

    @DeleteMapping
    public BrandOutputDto deleteBrand(@PathVariable Long brandId){
        return brandService.deleteBrand(brandId);
    }


}
