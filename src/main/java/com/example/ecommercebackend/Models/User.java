package com.example.ecommercebackend.Models;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@AllArgsConstructor
public class User {
    @NotEmpty(message = "User ID cannot be empty and must be set as a valid UUID.")
    @UUID(message = "Please enter a valid UUID") //// we only accept valid generated UUID.
    private String id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 6, message = "Username has to be at least 6 characters")
    private String username;


    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 7, message = "Password has to be at least 7 characters")
    @Pattern(regexp = "(?=[0-9])", message = "Password must contain characters and digitss")
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    @Email
    private String email;


    @NotEmpty(message = "Role cannot be empty.")
    @Pattern(regexp = "(?i)admin|customer", message = "You have to choose a valid role either Admin or Customer")
    private String role;

    @NotNull(message = "Balance cannot be left empty")
    @PositiveOrZero ///// I put or Zero instead of only Positive because new user could have 0 in their account balance.
    private Integer balance;

}
