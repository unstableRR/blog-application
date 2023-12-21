package com.blogapplication.controllers;

import com.blogapplication.exceptions.ApiException;
import com.blogapplication.payloads.JwtAuthRequest;
import com.blogapplication.payloads.UserDto;
import com.blogapplication.security.JwtAuthResponse;
import com.blogapplication.security.JwtTokenHelper;
import com.blogapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @Autowired
    private JwtTokenHelper jth;

    @Autowired
    private UserDetailsService uds;

    @Autowired
    private AuthenticationManager am;

    @Autowired
    private UserService userSer;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request
            ) throws Exception{

        this.authenticate(request.getUsername(), request.getPassword());

        UserDetails userDetails = this.uds.loadUserByUsername(request.getUsername());
        String token = this.jth.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception{

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            this.am.authenticate(authenticationToken);
        }catch(BadCredentialsException e){
            System.out.println("Invalid Details !");
            throw new ApiException("Invalid Username or Password...");
        }
    }

    //register new user api
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
        UserDto registeredUser = this.userSer.registerNewUser(userDto);

        return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
    }

}
