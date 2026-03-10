package com.fullstack.service;

import com.fullstack.entity.Customer;
import com.fullstack.exception.InsufficientFundException;
import com.fullstack.exception.RecordNotFoundException;
import com.fullstack.repository.CustomerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService implements ICustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String userName;

    @Override
    public Customer signUp(Customer customer) {
        customer.setCustPassword(passwordEncoder.encode(customer.getCustPassword()));
        return customerRepository.save(customer);
    }

    @Override
    public void depositAmount(Long custAccountNumber, double amount) throws MessagingException {

        Customer customer = customerRepository.findByCustAccountNumber(custAccountNumber).orElseThrow(() -> new RecordNotFoundException("Customer Account Number Does Not Exist."));

        double custAccountBalance = customer.getCustAccountBalance();


        custAccountBalance += amount;
        customer.setCustAccountBalance(custAccountBalance);

        customerRepository.save(customer);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(userName);

        mimeMessageHelper.setTo(customer.getCustEmailId());

        mimeMessageHelper.setSubject("Amount Deposited Successfully.");

        mimeMessageHelper.setText("After Deposit Customer Account Balance is : " + custAccountBalance);

        javaMailSender.send(mimeMessage);

        log.info("Mail Sent Successfully.");
    }

    @Override
    public Customer update(Long custId, Customer customer) {

        Customer customer1 = customerRepository.findById(custId).orElseThrow(()-> new RecordNotFoundException("Customer ID Does Not Exist"));

        customer1.setCustName(customer.getCustName());
        customer1.setCustAddress(customer.getCustAddress());
        customer1.setCustAccountNumber(customer.getCustAccountNumber());
        customer1.setCustAccountBalance(customer.getCustAccountBalance());
        customer1.setCustContactNumber(customer.getCustContactNumber());
        customer1.setCustDOB(customer.getCustDOB());
        customer1.setCustGender(customer.getCustGender());
        customer1.setCustUID(customer.getCustUID());
        customer1.setCustPanCard(customer.getCustPanCard());
        customer1.setCustEmailId(customer.getCustEmailId());
        customer1.setCustPassword(customer.getCustPassword());

        return customerRepository.save(customer1);
    }

    @Override
    public Optional<Customer> findById(Long custId) {
        return Optional.of(customerRepository.findById(custId).orElseThrow(() -> new RecordNotFoundException("Customer ID Does Not Exist")));
    }

    @Override
    public void withdrawAmount(Long custAccountNumber, double amount) throws MessagingException {

        Customer customer = customerRepository.findByCustAccountNumber(custAccountNumber).orElseThrow(() -> new RecordNotFoundException("Customer Account Number Does Not Exist."));

        double custAccountBalance = customer.getCustAccountBalance();

        if (customer.getCustAccountBalance() >= amount){
            custAccountBalance -= amount;
        } else {
            throw new InsufficientFundException("Insufficient Funds " + custAccountBalance + " Please try other amount.");
        }


        customer.setCustAccountBalance(custAccountBalance);

        customerRepository.save(customer);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(userName);

        mimeMessageHelper.setTo(customer.getCustEmailId());

        mimeMessageHelper.setSubject("Amount Withdraw Successfully.");

        mimeMessageHelper.setText("After Withdraw Customer Account Balance is : " + custAccountBalance);

        javaMailSender.send(mimeMessage);

        log.info("Mail Sent Successfully.");
    }
}
