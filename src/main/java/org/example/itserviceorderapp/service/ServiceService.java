package org.example.itserviceorderapp.service;

import org.example.itserviceorderapp.model.ITService;
import org.example.itserviceorderapp.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<ITService> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<ITService> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public ITService saveService(ITService service) {
        return serviceRepository.save(service);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}