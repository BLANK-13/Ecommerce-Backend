package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    //// DI to access DBs we need for now.
    private final ProductService productService;
    private final MerchantStockService merchantStockService;


    private final HashMap<String, User> usersDB = new HashMap<>();


    public ResponseEntity<ApiResponse<Collection<User>>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(usersDB.values()));
    }

    public ResponseEntity<ApiResponse<String>> addUser(User newUser) {

        //// only unique usernames are allowed.
        List<String> existingUsernames = new java.util.ArrayList<>(usersDB.values().stream()
                .map(User::getUsername)
                .map(String::toLowerCase)
                .toList());

        if (usersDB.containsKey(newUser.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This user was already added."));
        } else if (existingUsernames.contains(newUser.getUsername().toLowerCase())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This username already exists."));
        }

        String passwordEncryption = encryptPassword(newUser.getPassword());
        if (passwordEncryption == null) {
            //// this should not ever happen but just in case we'll handle it.
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ApiResponse<>("An unexpected error happened."));
        }


        userEncryptedPasswords.put(newUser.getUsername().toLowerCase(), new userPassEnrcyptedRecord(newUser.getId(), passwordEncryption));

        newUser.setPassword(passwordEncryption);
        usersDB.put(newUser.getId(), newUser);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User added successfully !"));
    }


    public ResponseEntity<ApiResponse<String>> updateUser(String username, User updatedUser) {

        //// only unique usernames are allowed.
        List<String> existingUsernames = new java.util.ArrayList<>(usersDB.values().stream()
                .map(User::getUsername)
                .map(String::toLowerCase)
                .toList());

        if (!usersDB.containsKey(updatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find a user with this ID in the database, double check the ID."));
        } else if (!existingUsernames.contains(username.toLowerCase())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find a user with this username in the database, double check the username."));
        }

        existingUsernames.remove(username.toLowerCase());
        //// if the username in the path variable doesn't belong to the user we don't allow editing.
        if (!usersDB.get(updatedUser.getId()).getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This username doesn't belong to the user ID you entered"));
        } else if (existingUsernames.contains(updatedUser.getUsername().toLowerCase())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("This username already exists."));
        }

        String passwordEncryption = encryptPassword(updatedUser.getPassword());
        if (passwordEncryption == null) {
            //// this should not ever happen but just in case we'll handle it.
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ApiResponse<>("An unexpected error happened."));
        }


        userPassEnrcyptedRecord tempInfo = new userPassEnrcyptedRecord(updatedUser.getId(), passwordEncryption);
        userEncryptedPasswords.remove(username);
        userEncryptedPasswords.put(updatedUser.getUsername().toLowerCase(), tempInfo);


        updatedUser.setPassword(passwordEncryption);
        usersDB.replace(updatedUser.getId(), updatedUser);


        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User info updated successfully !"));
    }

    public ResponseEntity<ApiResponse<String>> deleteUser(String queryID) {

        if (!usersDB.containsKey(queryID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>("Couldn't find a user with this ID in the database, double check the ID."));
        }

        usersDB.remove(queryID);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("User deleted successfully !"));
    }

    public ResponseEntity<ApiResponse<String>> buyProduct(String userID, String merchantStockID, Integer amountToBuy) {

        var stockDbSnapshot = merchantStockService.dbSnapshot();
        var productDbSnapshot = productService.dbSnapshot();

        if (!usersDB.containsKey(userID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find a user with this ID in the database, double check the ID."));
        } else if (!stockDbSnapshot.containsKey(merchantStockID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Couldn't find a merchant stock with this ID in the database double check the ID."));
        } else if (amountToBuy < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Amount to buy has to be a positive number."));
        }

        Integer currentStockAmount = stockDbSnapshot.get(merchantStockID).getStock();
        String productId = stockDbSnapshot.get(merchantStockID).getProductId();
        Double productPrice = productDbSnapshot.get(productId).getPrice();
        Double userCurrentBalance = usersDB.get(userID).getBalance();
        Double finalPrice = productPrice * amountToBuy;

        if (userCurrentBalance < finalPrice) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Your balance of " + userCurrentBalance.intValue() + " is insufficient to buy " + amountToBuy + " of " + productDbSnapshot.get(productId).getName() + " each item is priced at " + productPrice));
        } else if (currentStockAmount < amountToBuy) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Stock is not enough to buy this amount, the amount maximum to buy is " + currentStockAmount));
        }
        merchantStockService.stockDB.get(merchantStockID).setStock(currentStockAmount - amountToBuy);
        usersDB.get(userID).setBalance(userCurrentBalance - finalPrice);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(amountToBuy + " of " + productDbSnapshot.get(productId).getName() + " were purchased successfully !"));


    }

    //// extra credit

    public ResponseEntity<ApiResponse> getUserInfo(String queryID) {

        if (!usersDB.containsKey(queryID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>("Couldn't find a user with this ID in the database, double check the ID."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<User>(usersDB.get(queryID)));
    }

    public ResponseEntity<ApiResponse<String>> addBalance(String queryID, Double amountToAdd) {

        if (!usersDB.containsKey(queryID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>("Couldn't find a user with this ID in the database, double check the ID."));
        } else if (amountToAdd < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Amount to add has to be a positive number."));
        }

        double newBalance = usersDB.get(queryID).getBalance() + amountToAdd;
        usersDB.get(queryID).setBalance(newBalance);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(amountToAdd + " SAR was added successfully to your wallet !"));
    }

    public ResponseEntity<ApiResponse> userLogin(String username, String password) {

        if (!userEncryptedPasswords.containsKey(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>("Couldn't find a user with this username in the database, double check the username."));
        }

        User loginUser = usersDB.get(userEncryptedPasswords.get(username).userID());
        String pass = encryptPassword(password);

        if (!pass.equals(loginUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<String>("Password is incorrect try another one."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<User>(loginUser));
    }


    //// will be used for the login to validate user password without storing it and knowing any users' password.

    private final HashMap<String, userPassEnrcyptedRecord> userEncryptedPasswords = new HashMap<>();

    private record userPassEnrcyptedRecord(String userID, String encryptedPass) {
    }

    private String encryptPassword(String password) {

        String encryptedpassword = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");

            m.update(password.getBytes());

            byte[] bytes = m.digest();

            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            encryptedpassword = s.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); //// this should never be reached anyway.
        }

        return encryptedpassword;
    }
}
