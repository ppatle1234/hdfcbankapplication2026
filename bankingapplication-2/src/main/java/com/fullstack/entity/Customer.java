package com.fullstack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER_DETAIL")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long custId;

    @Size(min = 2, message = "Customer Name Should be At Least 2 Characters")
    private String custName;

    private Long custAccountNumber;

    @NotBlank(message = "Address Should Not be Blank or Empty")
    private String custAddress;

    @Range(min = 1000000000, max = 9999999999L, message = "Contact Number Must be 10 digit")
    private Long custContactNumber;

    private double custAccountBalance;

    private String custGender;

    private LocalDate custDOB;

    private String custPanCard;

    private Long custUID;

    @Email(message = "Email ID Must be Valid")
    private String custEmailId;

    @Size(min = 4, message = "Password must be 4 Characters")
    private String custPassword;
}
