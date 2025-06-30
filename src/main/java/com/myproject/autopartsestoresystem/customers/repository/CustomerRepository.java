package com.myproject.autopartsestoresystem.customers.repository;

import com.myproject.autopartsestoresystem.customers.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

   Optional<Customer> findByEmail(String email);
}
