package com.fullstack.service;

import com.fullstack.entity.Customer;
import com.fullstack.exception.InsufficientFundException;
import com.fullstack.exception.InvalidOTPException;
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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService implements ICustomerService {

    Map<Long, Long> otpMap = new LinkedHashMap<>();

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

    @Override
    public void initiatefundTransfer(long fromAccountNumber, long toAccountNumber, double amount) throws MessagingException {

       Customer customer1 = customerRepository.findByCustAccountNumber(fromAccountNumber).get();

      // Customer customer2 = customerRepository.findByCustAccountNumber(toAccountNumber).get();

       if (customer1.getCustAccountBalance() >= amount){
           // initiate fund transfer - Generate Otp

           SecureRandom secureRandom = new SecureRandom();
           long otp = 100000 + secureRandom.nextInt(900000);

           otpMap.put(fromAccountNumber, otp);

           MimeMessage mimeMessage = javaMailSender.createMimeMessage();

           MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

           mimeMessageHelper.setFrom(userName);

           mimeMessageHelper.setTo(customer1.getCustEmailId());

           mimeMessageHelper.setSubject("OTP For Fund Transfer");

           mimeMessageHelper.setText("For Fund Transfer OTP : " + otp);

           javaMailSender.send(mimeMessage);

           log.info("OTP Send By Email : " + customer1.getCustEmailId());

       } else {
           throw new InsufficientFundException("Insufficient Funds.");
       }
    }

    @Override
    public void validateOTP(long fromAccountNumber, long toAccountNumber, double amount, long otp) throws MessagingException {

        Customer customer1 = customerRepository.findByCustAccountNumber(fromAccountNumber).get();

        Customer customer2 = customerRepository.findByCustAccountNumber(toAccountNumber).get();

        long existingOTP = otpMap.get(fromAccountNumber);

        if(existingOTP == otp){
            // transfer funds

            double fromCustAccountBalance = customer1.getCustAccountBalance();

            fromCustAccountBalance -= amount;

            customer1.setCustAccountBalance(fromCustAccountBalance);

            customerRepository.save(customer1);

            double toCustAccountBalance = customer2.getCustAccountBalance();

            toCustAccountBalance += amount;

            customer2.setCustAccountBalance(toCustAccountBalance);

            customerRepository.save(customer2);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(userName);

            mimeMessageHelper.setTo(customer1.getCustEmailId());

            mimeMessageHelper.setSubject("Amount Transfer Successfully.");

            mimeMessageHelper.setText("After Transfer Remaining Account balance is : " + fromCustAccountBalance);

            javaMailSender.send(mimeMessage);

            log.info("Email Send Successfully. " + customer1.getCustEmailId());

            // Notify Beneficiary Credit Information
            creditFundNotification(toAccountNumber);

        } else {
            throw new InvalidOTPException("Invalid OTP");
        }


    }

    void creditFundNotification(long toAccountNumber) throws MessagingException {

        Customer customer2 = customerRepository.findByCustAccountNumber(toAccountNumber).get();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(userName);

        mimeMessageHelper.setTo(customer2.getCustEmailId());

        mimeMessageHelper.setSubject("Amount Credited Successfully.");

        mimeMessageHelper.setText("After Credited Account balance is : " + customer2.getCustAccountBalance());

        javaMailSender.send(mimeMessage);

        log.info("Email Send Successfully. " + customer2.getCustEmailId());
    }
}
