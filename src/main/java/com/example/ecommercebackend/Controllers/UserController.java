package com.example.ecommercebackend.Controllers;


import com.example.ecommercebackend.ApiReponse.ApiResponse;
import com.example.ecommercebackend.Models.User;
import com.example.ecommercebackend.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Collection<User>>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addUser(@RequestBody @Valid User newUser, Errors errors) {
        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        return userService.addUser(newUser);
    }


    //// update by username not ID here.
    @PutMapping("/update/{username}")
    public ResponseEntity<ApiResponse<String>> updateUser(@PathVariable String username, @RequestBody @Valid User newUser, Errors errors) {

        if (errors.hasErrors()) {
            String errMessage = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(errMessage));
        }

        return userService.updateUser(username, newUser);
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String ID) {

        return userService.deleteUser(ID);
    }

    ////12.
    //// I chose accepting userID and merchant stock ID and then can access productID & merchantID from it more convenient this way. extra is accepting amount to buy and checking if it's available in stock.
    //// get the merchant that has stock and put id and amount you want and buy any product.
    @GetMapping("/buy/{userID}/{merchantStockID}/{amount}")
    public ResponseEntity<ApiResponse<String>> buyProduct(@PathVariable String userID, @PathVariable String merchantStockID, @PathVariable Integer amount) {

        return userService.buyProduct(userID, merchantStockID, amount);
    }


    //// extra credit

    //// get all my user info by ID shouldn't be by user so no one can access some else's info.
    @GetMapping("/my-info/{ID}")
    public ResponseEntity<ApiResponse> getUserInfo(@PathVariable String ID) {

        return userService.getUserInfo(ID);
    }

    //// this endpoint is for easy balance adding just /userID/amount you want to add to balance.
    @PutMapping("/add-balance/{ID}/{amount}")
    public ResponseEntity<ApiResponse<String>> addBalance(@PathVariable String ID, @PathVariable Double amount) {

        return userService.addBalance(ID, amount);
    }


    /// login and validating without storing user password and if the login is successful we'll display user info else we tell the user what is incorrect.
    @GetMapping("/login/{username}/{password}")
    public ResponseEntity<ApiResponse> userLogin(@PathVariable String username, @PathVariable String password) {

        return userService.userLogin(username, password);
    }
}
