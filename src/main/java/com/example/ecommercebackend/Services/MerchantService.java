package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.Merchant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MerchantService {


    private final HashMap<String, Merchant> merchantsDB = new HashMap<>();


    public ResponseEntity<ApiResponse<Collection<Merchant>>> getAllMerchants() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(merchantsDB.values()));
    }

    public ResponseEntity<ApiResponse<String>> addMerchant(Merchant newMerchant) {

        if (merchantsDB.containsKey(newMerchant.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This merchant was already added."));
        }

        merchantsDB.put(newMerchant.getId(), newMerchant);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Merchant added successfully !"));
    }


    public ResponseEntity<ApiResponse<String>> updateMerchant(String updateID, Merchant updatedMerchant) {

        if (!merchantsDB.containsKey(updateID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find a merchant with this ID in the database double check the ID."));
        } else if (!updatedMerchant.getId().equals(updateID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("You entered a new ID for the updated merchant, IDs cannot be changed."));
        }


        merchantsDB.replace(updateID, updatedMerchant);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Merchant info updated successfully !"));
    }


    public ResponseEntity<ApiResponse<String>> deleteMerchant(String queryID) {

        if (!merchantsDB.containsKey(queryID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find a merchant with this ID in the database double check the ID."));
        }

        merchantsDB.remove(queryID);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Merchant deleted successfully !"));
    }


    //// extra credit

    //// for extra in the product and merchant stock , we get a snapshot so we guarantee the data of the DB are only mutable here.
    public HashMap<String, Merchant> dbSnapshot() {
        return merchantsDB;
    }


    public ResponseEntity<ApiResponse<Collection<Merchant>>> getSortedMerchants() {
        List<Merchant> sortedCategories = new ArrayList<>(merchantsDB.values());

        sortedCategories.sort(Comparator.comparing(Merchant::getName, String.CASE_INSENSITIVE_ORDER));


        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(sortedCategories));
    }

}
