package org.example.itserviceorderapp.repository;

import org.example.itserviceorderapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Example: Find by email (unique constraint recommended)
    Optional<Customer> findByEmail(String email);

    // Example: Find all customers by (partial) name, ignoring case
    List<Customer> findByNameContainingIgnoreCase(String name);
}
