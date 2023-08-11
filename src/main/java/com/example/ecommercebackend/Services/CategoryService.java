package com.example.ecommercebackend.Services;


import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private final HashMap<Integer, Category> categoryDB = new HashMap<>() {{
        put(1, new Category(1, "Books"));
        put(2, new Category(2, "Foods"));
        put(3, new Category(3, "Gadgets"));
        put(4, new Category(4, "Clothing"));
    }};


    public ResponseEntity<ApiResponse<Collection<Category>>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(categoryDB.values()));
    }

    public ResponseEntity<ApiResponse<String>> addCategory(Category newCategory) {

        ///// since we will not add / update categories very often like products for example I think it's acceptable to make a list of the existing categories to check.
        //// this is basically to not add Book / book / AND to check if a category name exists.
        List<String> existingCategoryNames = categoryDB.values().stream()
                .map(Category::getName)
                .map(String::toLowerCase)
                .toList();

        if (categoryDB.containsKey(newCategory.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This category ID already exists."));
        } else if (existingCategoryNames.contains(newCategory.getName().toLowerCase())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This category name already exists."));
        }


        categoryDB.put(newCategory.getId(), newCategory);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(newCategory.getName() + " category added successfully !"));
    }

    public ResponseEntity<ApiResponse<String>> updateCategory(Integer updateID, Category updatedCategory) {

        ///// since we will not add / update categories very often like products for example I think it's acceptable to make a list of the existing categories to check.
        //// this is basically to not add Book / book / AND for checking if the user give valid new ID but enters a category name that exists.
        List<String> existingCategoryNames = categoryDB.values().stream()
                .map(Category::getName)
                .map(String::toLowerCase)
                .toList();

        if (!categoryDB.containsKey(updateID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find the category with this ID in the database double check the ID."));
        } else if (categoryDB.containsKey(updatedCategory.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This category ID already exists."));
        } else if (existingCategoryNames.contains(updatedCategory.getName().toLowerCase())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This category name already exists."));
        }


//// the IDs are allowed to be different here so we can't use .replace()
        categoryDB.remove(updateID);
        categoryDB.put(updatedCategory.getId(), updatedCategory);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Category updated successfully !"));
    }

    public ResponseEntity<ApiResponse<String>> deleteCategory(Integer queryID) {

        if (!categoryDB.containsKey(queryID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find the category with this ID in the database double check the ID."));
        }

        categoryDB.remove(queryID);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Category deleted successfully !"));
    }



    //// extra credit

    //// for extra in the product , we get a snapshot so we guarantee the data of the DB are only mutable here.
    public HashMap<Integer, Category> dbSnapshot() {
        return categoryDB;
    }


    public ResponseEntity<ApiResponse<Collection<Category>>> getSortedCategories() {
        List<Category> sortedCategories = new ArrayList<>(categoryDB.values());

        sortedCategories.sort(Comparator.comparing(Category::getName,String.CASE_INSENSITIVE_ORDER));


        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(sortedCategories));
    }




}
