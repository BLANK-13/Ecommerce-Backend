package com.example.ecommercebackend.ApiReponse;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ApiResponse<T> {

    //// I used a generic here so I can always return a response with this attribute, if it's 200 the frontend will get what they want either an Object of an entity(Product, Category etc..) or just a String message they need.
    private T response;
}