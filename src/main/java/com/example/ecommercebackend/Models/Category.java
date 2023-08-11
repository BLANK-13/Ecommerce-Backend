package com.example.ecommercebackend.Models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

    @NotNull(message = "category ID cannot be empty, it must be set to a positive number")
    @Min(value = 1, message = "category ID has to be a positive number")
    private Integer id; //// we'll have a set of categories as integers eg. 1 = books etc...


    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, message = "Name has to be at least three characters")
    private String name;


}
