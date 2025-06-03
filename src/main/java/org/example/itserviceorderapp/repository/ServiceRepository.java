package org.example.itserviceorderapp.repository;

import org.example.itserviceorderapp.model.ITService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ITService, Long> {
}