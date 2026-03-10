package com.fullstack.controller;

import com.fullstack.entity.Customer;
import com.fullstack.service.ICustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Banking Application", description = "APIs of Customer Controller")
@SecurityRequirement(name = "Bearer Auth")
public class CustomerController {

    private final ICustomerService customerService;

    @PatchMapping("/deposit/{custAccountNumber}/{amount}")
    public ResponseEntity<String> depositAmount(@PathVariable Long custAccountNumber, @PathVariable double amount) throws MessagingException {

        customerService.depositAmount(custAccountNumber, amount);
        return new ResponseEntity<>("Amount Deposited Successfully.", HttpStatus.CREATED);
    }

    @PatchMapping("/withdraw/{custAccountNumber}/{amount}")
    public ResponseEntity<String> withdrawAmount(@PathVariable Long custAccountNumber, @PathVariable double amount) throws MessagingException {

        customerService.withdrawAmount(custAccountNumber, amount);
        return new ResponseEntity<>("Amount Withdraw Successfully.", HttpStatus.CREATED);
    }

    @PutMapping("/update/{custId}")
    public ResponseEntity<Customer> update(@PathVariable Long custId, @RequestBody Customer customer){

        return new ResponseEntity<>(customerService.update(custId, customer), HttpStatus.CREATED);
    }

    @GetMapping("/findbyid/{custId}")
    public ResponseEntity<?> findById(@PathVariable Long custId){
        return new ResponseEntity<>(customerService.findById(custId), HttpStatus.OK);
    }

    @PatchMapping("/initiatefundtransfer/{fromAccountNumber}/{toAccountNumber}/{amount}")
    public ResponseEntity<String> initiateFundTransfer(@PathVariable long fromAccountNumber, @PathVariable long toAccountNumber, @PathVariable double amount) throws MessagingException {
        customerService.initiatefundTransfer(fromAccountNumber, toAccountNumber, amount);
        return new ResponseEntity<>("OTP Send Successfully. ", HttpStatus.OK);
    }

    @PatchMapping("/validateotp/{fromAccountNumber}/{toAccountNumber}/{amount}/{otp}")
    public ResponseEntity<String> validateOTP(@PathVariable long fromAccountNumber, @PathVariable long toAccountNumber, @PathVariable double amount, @PathVariable long otp) throws MessagingException {

        customerService.validateOTP(fromAccountNumber, toAccountNumber,amount, otp);
        return new ResponseEntity<>("OTP Validated", HttpStatus.OK);
    }
}
