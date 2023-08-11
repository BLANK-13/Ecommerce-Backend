package com.example.ecommercebackend.Models;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.hibernate.validator.constraints.UUID;

@Data
@AllArgsConstructor
public class Product {


    @NotEmpty(message = "Product ID cannot be empty and must be set as a valid UUID.")
    @UUID(message = "Please enter a valid UUID") //// we only accept valid generated UUID.
    private String id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, message = "Name has to be at least three characters")
    private String name;

    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price has to be a positive number")

    private Double price;

    @NotNull(message = "category ID cannot be empty")
    @Positive(message = "category ID has to be a positive number")
    private Integer categoryID;
}
