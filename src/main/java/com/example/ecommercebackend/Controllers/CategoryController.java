package com.example.ecommercebackend.Controllers;

import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.Category;
import com.example.ecommercebackend.Services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;


@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Collection<Category>>> getAllProducts() {
        return categoryService.getAllCategories();
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addCategory(@RequestBody @Valid Category newCategory, Errors errors) {
        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }
        return categoryService.addCategory(newCategory);
    }

    @PutMapping("/update/{ID}")
    public ResponseEntity<ApiResponse<String>> updateCategory(@PathVariable Integer ID, @RequestBody @Valid Category categoryUpdate, Errors errors) {

        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        return categoryService.updateCategory(ID, categoryUpdate);
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Integer ID) {

        return categoryService.deleteCategory(ID);
    }

}
