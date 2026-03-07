package com.fullstack.controller;

import com.fullstack.dto.LoginRequest;
import com.fullstack.entity.Customer;
import com.fullstack.service.CustomUserService;
import com.fullstack.service.CustomerService;
import com.fullstack.service.ICustomerService;
import com.fullstack.util.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ICustomerService customerService;

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<Customer> signUp(@RequestBody @Valid Customer customer){
        return new ResponseEntity<>(customerService.signUp(customer), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> generateToken(@RequestBody LoginRequest loginRequest){
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.custEmailId(), loginRequest.custPassword()));

         return new ResponseEntity<>(jwtUtil.generateToken(loginRequest.custEmailId()), HttpStatus.OK);
    }
}
