package com.example.ecommercebackend.Services;


import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.MerchantStock;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    //// DI to access DBs we need for now.
    private final ProductService productService;
    private final MerchantService merchantService;


    //// this will be our virtual db that contains a unique UUID for MerchantStock as key and a record of StockItemInto that contains all the items we need as a value.
    protected final HashMap<String, MerchantStock> stockDB = new HashMap<>();


    public ResponseEntity<ApiResponse<Collection<MerchantStock>>> getAllMerchantStock() {

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(stockDB.values()));
    }

    public ResponseEntity<ApiResponse<String>> addMerchantStock(MerchantStock newMerchantStock) {

        if (stockDB.containsKey(newMerchantStock.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("a merchant stock with this ID was already added, try updating it or double check the ID."));
        }

        //// validate ID fields and if they are in the DBs, if the user enters IDs but these products or merchants are not in the DB we shouldn't make a merchant stock of non-existent merchants or products.
        final String errMessage = validateStockItemInfo(newMerchantStock);
        if (errMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        stockDB.put(newMerchantStock.getId(), newMerchantStock);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("New merchant stock added successfully !"));
    }

    public ResponseEntity<ApiResponse<String>> updateMerchantStock(String updateID, MerchantStock updatedMerchantStock) {
        if (!stockDB.containsKey(updateID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find a merchant stock with this ID in the database double check the ID."));
        } else if (!updatedMerchantStock.getId().equals(updateID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("You entered a new ID for the updated merchant stock, IDs cannot be changed."));
        }

        //// validate ID fields and if they are in the DBs, if the user enters IDs but these products or merchants are not in the DB we shouldn't make a merchant stock of non-existent merchants or products.
        final String errMessage = validateStockItemInfo(updatedMerchantStock);
        if (errMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        stockDB.replace(updateID, updatedMerchantStock);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Merchant stock updated successfully !"));
    }

    public ResponseEntity<ApiResponse<String>> deleteMerchantStock(String queryID) {

        if (!stockDB.containsKey(queryID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find a merchant stock with this ID in the database double check the ID."));
        }

        stockDB.remove(queryID);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Merchant stock deleted successfully !"));
    }


    public ResponseEntity<ApiResponse<String>> addToStock(String queryID, Integer amountToAdd) {

        if (!stockDB.containsKey(queryID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find a merchant stock with this ID in the database double check the ID."));
        } else if (amountToAdd < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Amount to add has to be a positive number."));

        }

        Integer newStock = stockDB.get(queryID).getStock() + amountToAdd;
        stockDB.get(queryID).setStock(newStock);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(amountToAdd + " of " + productService.dbSnapshot().get(queryID).getName() + " product successfully added to stock !"));
    }

    //// extra credit
    public ResponseEntity<ApiResponse> getMyStock(String queryID) {

        if (!stockDB.containsKey(queryID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>("Couldn't find a merchant stock with this ID in the database double check the ID."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<MerchantStock>(stockDB.get(queryID)));
    }


    ///// we check if the productId and merchantId are valid and we actually have them in their own DBs.
    public String validateStockItemInfo(MerchantStock merchantStock) {
        if (!productService.dbSnapshot().containsKey(merchantStock.getProductId())) {
            return "invalid product ID, it doesn't exist in the products database.";
        } else if (!merchantService.dbSnapshot().containsKey(merchantStock.getMerchantId())) {
            return "invalid merchant ID, it doesn't exist in the merchants database.";
        }
        return null; //// if we reach this point then it's valid that means the error message is null and both IDs are valid and exist in our DBs.
    }

    public HashMap<String, MerchantStock> dbSnapshot() {
        return stockDB;
    }
}
