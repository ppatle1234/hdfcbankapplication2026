package com.fullstack.repository;

import com.fullstack.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // JPAQL

    Customer findByCustEmailId(String custEmailId);
}
