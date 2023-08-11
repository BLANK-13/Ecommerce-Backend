package com.example.ecommercebackend.Models;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@AllArgsConstructor
public class MerchantStock {

    @NotEmpty(message = "Product ID cannot be empty and must be set as a valid UUID.")
    @UUID(message = "Please enter a valid UUID") //// we only accept valid generated UUID.
    private String id;


    @NotEmpty(message = "Product ID cannot be empty and must be set as a valid UUID.")
    @UUID(message = "Please enter a valid UUID") //// we only accept valid generated UUID.
    private String productId;


    @NotEmpty(message = "Merchant ID cannot be empty and must be set as a valid UUID.")
    @UUID(message = "Please enter a valid UUID") //// we only accept valid generated UUID.
    private String merchantId;



    @NotNull(message = "Stock quantity cannot be empty")
    @Min(value = 11, message = "The minimum stock allowed is 11.")
    private Integer stock;
}
