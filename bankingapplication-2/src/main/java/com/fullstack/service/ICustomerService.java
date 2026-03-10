package com.fullstack.service;

import com.fullstack.entity.Customer;
import jakarta.mail.MessagingException;

import java.util.Optional;

public interface ICustomerService {

    Customer signUp(Customer customer);

    void depositAmount(Long custAccountNumber, double amount) throws MessagingException;

    Customer update(Long custId, Customer customer);

    Optional<Customer> findById(Long custId);

    void withdrawAmount(Long custAccountNumber, double amount) throws MessagingException;

    void initiatefundTransfer(long fromAccountNumber, long toAccountNumber, double amount) throws MessagingException;

    void validateOTP(long fromAccountNumber, long toAccountNumber, double amount, long otp) throws MessagingException;
}
