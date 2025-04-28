package ru.sirius.hl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sirius.hl.dto.CustomerDto;
import ru.sirius.hl.service.CustomerService;
import ru.sirius.hl.service.ObservabilityService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Tag(name = "Customer Controller")
public class CustomerController {

    private final CustomerService customerService;
    private final ObservabilityService observabilityService;

    @DeleteMapping("/clear")
    @Operation(summary = "Очистка всех пользователей и связанных билетов")
    public ResponseEntity<String> clearAll() {
        long startTime = observabilityService.startTiming();
        try {
            customerService.clearAll();
            return ResponseEntity.ok("All customers and related tickets cleared");
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @GetMapping
    @Operation(summary = "Получение всех пользователей без пагинации")
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        long startTime = observabilityService.startTiming();
        try {
            List<CustomerDto> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по ID")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        long startTime = observabilityService.startTiming();
        try {
            CustomerDto customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Физическое удаление пользователя")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        long startTime = observabilityService.startTiming();
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @PostMapping
    @Operation(summary = "Создание нового пользователя")
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customer) {
        long startTime = observabilityService.startTiming();
        try {
            CustomerDto savedCustomer = customerService.saveCustomer(customer);
            return ResponseEntity.ok(savedCustomer);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление существующего пользователя")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customer) {
        long startTime = observabilityService.startTiming();
        try {
            CustomerDto updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }
}