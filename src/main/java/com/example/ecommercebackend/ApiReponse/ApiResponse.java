package com.example.ecommercebackend.ApiReponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private T response;
}