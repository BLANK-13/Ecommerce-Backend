package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {


    //// DI to access DBs we need for now.
    private final CategoryService categoryService;


    private final HashMap<String, Product> productsDB = new HashMap<>();


    ///// minimum requirements
    public ResponseEntity<ApiResponse<Collection<Product>>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(productsDB.values()));
    }

    public ResponseEntity<ApiResponse<String>> addProduct(Product newProduct) {

        if (productsDB.containsKey(newProduct.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This product was already added."));
        } else if (!categoryService.dbSnapshot().containsKey(newProduct.getCategoryID())) { //// get available categories and check it.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("a category with this ID doesn't exist visit /category/get to see the available categories."));
        }

        productsDB.put(newProduct.getId(), newProduct);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Product added successfully !"));
    }

    public ResponseEntity<ApiResponse<String>> updateProduct(String updateID, Product updatedProduct) {
        if (!productsDB.containsKey(updateID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find the product in the database double check the ID."));
        } else if (!updatedProduct.getId().equals(updateID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("You entered a new ID for the updated product, IDs cannot be changed."));
        }


        productsDB.replace(updateID,updatedProduct);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Product updated successfully !"));
    }

    public ResponseEntity<ApiResponse<String>> deleteProduct(String queryID) {

        if (!productsDB.containsKey(queryID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find the product in the database double check the exact product ID."));
        }

        productsDB.remove(queryID);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Product deleted successfully !"));
    }


    ///// extra credit

    public ResponseEntity<ApiResponse> searchProductsByName(String searchName) {
        List<Product> searchMatchProducts = productsDB.values().stream()
                .filter(product -> Objects.equals(searchName, product.getName()))
                .toList();

        if (searchMatchProducts.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>("Couldn't find a product with this name."));

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<List<Product>>(searchMatchProducts));
    }

    public ResponseEntity<ApiResponse> getProductsById(Integer id) {

        if (!categoryService.dbSnapshot().containsKey(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>("a category with this ID doesn't exist visit /category/get to see the available categories."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<Collection<Product>>(productsDB.values().stream()
                .filter(product -> Objects.equals(id, product.getCategoryID()))
                .toList()));
    }

    //// for extra in merchant stock , we get a snapshot so we guarantee the data of the DB are only mutable here.
    public HashMap<String, Product> dbSnapshot() {
        return productsDB;
    }
}
