package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

   Optional<Customer> findByEmail(String email);
}
