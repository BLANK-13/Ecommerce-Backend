package com.example.ecommercebackend.Controllers;

import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.MerchantStock;
import com.example.ecommercebackend.Services.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/stock")
@RequiredArgsConstructor
public class MerchantStockController {


    private final MerchantStockService merchantStockService;


    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Collection<MerchantStock>>> getAllMerchantStock() {
        return merchantStockService.getAllMerchantStock();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addMerchantStock(@RequestBody @Valid MerchantStock newMerchantStock, Errors errors) {
        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        return merchantStockService.addMerchantStock(newMerchantStock);
    }

    @PutMapping("/update/{ID}")
    public ResponseEntity<ApiResponse<String>> updateMerchantStock(@PathVariable String ID, @RequestBody @Valid MerchantStock merchantStockUpdate, Errors errors) {

        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        return merchantStockService.updateMerchantStock(ID, merchantStockUpdate);
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<ApiResponse<String>> deleteMerchantStock(@PathVariable String ID) {

        return merchantStockService.deleteMerchantStock(ID);
    }
    ///// 11.

    //// I chose only accepting merchant stock ID and then can access productID & merchantID from it more convenient this way.
    @PutMapping("/add-stock/{ID}/{amount}")
    public ResponseEntity<ApiResponse<String>> addToStock(@PathVariable String ID, @PathVariable Integer amount) {

        return merchantStockService.addToStock(ID, amount);
    }


    //// extra credit
    @GetMapping("/my-stock/{ID}")
    public ResponseEntity<ApiResponse> getMyStock(@PathVariable String ID) {

        return merchantStockService.getMyStock(ID);
    }


}
