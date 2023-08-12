package com.example.ecommercebackend.Controllers;


import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.Product;
import com.example.ecommercebackend.Services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Collection<Product>>> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addProduct(@RequestBody @Valid Product newProduct, Errors errors) {
        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }
        return productService.addProduct(newProduct);
    }

    @PutMapping("/update/{ID}")
    public ResponseEntity<ApiResponse<String>> updateProduct(@PathVariable String ID, @RequestBody @Valid Product productUpdate, Errors errors) {

        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        return productService.updateProduct(ID, productUpdate);
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable String ID) {

        return productService.deleteProduct(ID);
    }



    ///// Extra credit.


    //// search a product by name a get a list of all matching products
    @GetMapping("/search/{searchName}")
    public ResponseEntity<ApiResponse> searchProductsByName(@PathVariable String searchName) {

        return productService.searchProductsByName(searchName);
    }

    //// get all products in x category
    @GetMapping("/get-category/{id}")
    public ResponseEntity<ApiResponse> getProductsById(@PathVariable Integer id) {

        return productService.getProductsById(id);
    }

}
