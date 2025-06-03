package org.example.itserviceorderapp.controller;

import org.example.itserviceorderapp.model.ITService;
import org.example.itserviceorderapp.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public List<ITService> getAllServices() {
        return serviceService.getAllServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ITService> getServiceById(@PathVariable Long id) {
        Optional<ITService> service = serviceService.getServiceById(id);
        return service.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ITService> createService(@RequestBody ITService service) {
        ITService savedService = serviceService.saveService(service);
        return new ResponseEntity<>(savedService, HttpStatus.CREATED); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<ITService> updateService(@PathVariable Long id, @RequestBody ITService serviceDetails) {
        Optional<ITService> existingService = serviceService.getServiceById(id);
        if (existingService.isPresent()) {
            ITService serviceToUpdate = existingService.get();
            serviceToUpdate.setName(serviceDetails.getName());
            serviceToUpdate.setDescription(serviceDetails.getDescription());
            serviceToUpdate.setPrice(serviceDetails.getPrice());
            ITService updatedService = serviceService.saveService(serviceToUpdate);
            return ResponseEntity.ok(updatedService);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Xizmatni o'chirish (DELETE /api/services/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        Optional<ITService> service = serviceService.getServiceById(id);
        if (service.isPresent()) {
            serviceService.deleteService(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}