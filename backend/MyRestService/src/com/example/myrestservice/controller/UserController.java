package com.example.myrestservice.controller;

import com.example.myrestservice.FileStorage.FileStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.myrestservice.FileStorage.UserDto;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {
        try {
            // Call method to store user credentials
            FileStorage.saveCredentials(userDto.getUsername(), userDto.getPassword());

            // Return successful response
            return ResponseEntity.ok("Registration successful");
        } catch (Exception e) {
            // Handle exceptions and return appropriate response
            return ResponseEntity.internalServerError().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
        boolean isValidUser = FileStorage.verifyCredentials(userDto.getUsername(), userDto.getPassword());
        if (isValidUser) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
