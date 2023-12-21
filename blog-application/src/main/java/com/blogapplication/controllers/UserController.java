package com.blogapplication.controllers;

import com.blogapplication.payloads.ApiResponse;
import com.blogapplication.payloads.UserDto;
import com.blogapplication.services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    //POST - create user
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){

        UserDto createUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    //PUT - update user
    @PutMapping ("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("id") Integer id){
        UserDto updatedUser = this.userService.updateUser(userDto, id);

        return ResponseEntity.ok(updatedUser);
    }

    //ADMIN
    //DELETE - delete user
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") Integer id){
            this.userService.deleteUser(id);

            return new ResponseEntity<>(new ApiResponse("User deleted Successfully", true), HttpStatus.OK);
    }

    //GET - all users
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> users = this.userService.getAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //GET - user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id){
        UserDto user = this.userService.getUserById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
