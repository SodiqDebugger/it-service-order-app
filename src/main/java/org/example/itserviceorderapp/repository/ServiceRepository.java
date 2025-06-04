package org.example.itserviceorderapp.repository;

import org.example.itserviceorderapp.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    // Example: Find services by name containing a keyword (case insensitive)
    List<Service> findByNameContainingIgnoreCase(String name);

    // Example: Find by status if your Service entity has a status field
    // List<Service> findByStatus(String status);
}
