package com.example.ecommercebackend.Controllers;

import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.Merchant;
import com.example.ecommercebackend.Services.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Collection<Merchant>>> getAllMerchants() {
        return merchantService.getAllMerchants();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addMerchant(@RequestBody @Valid Merchant newMerchant, Errors errors) {
        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        return merchantService.addMerchant(newMerchant);
    }

    @PutMapping("/update/{ID}")
    public ResponseEntity<ApiResponse<String>> updateMerchant(@PathVariable String ID, @RequestBody @Valid Merchant merchantUpdate, Errors errors) {

        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        return merchantService.updateMerchant(ID, merchantUpdate);
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<ApiResponse<String>> deleteMerchant(@PathVariable String ID) {

        return merchantService.deleteMerchant(ID);
    }


    ///// extra credit
    @GetMapping("/sorted")
    public ResponseEntity<ApiResponse<Collection<Merchant>>> getSortedMerchants() {

        return merchantService.getSortedMerchants();
    }
}

