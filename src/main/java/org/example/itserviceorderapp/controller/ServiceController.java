package org.example.itserviceorderapp.controller;

import org.example.itserviceorderapp.model.ITService;
import org.example.itserviceorderapp.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public List<ITService> getAllServices() {
        return serviceService.getAllServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ITService> getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ITService> createService(@RequestBody ITService service) {
        ITService savedService = serviceService.saveService(service);
        return new ResponseEntity<>(savedService, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ITService> updateService(@PathVariable Long id, @RequestBody ITService serviceDetails) {
        return serviceService.getServiceById(id)
                .map(existingService -> {
                    existingService.setName(serviceDetails.getName());
                    existingService.setDescription(serviceDetails.getDescription());
                    existingService.setPrice(serviceDetails.getPrice());
                    ITService updated = serviceService.saveService(existingService);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(service -> {
                    serviceService.deleteService(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
