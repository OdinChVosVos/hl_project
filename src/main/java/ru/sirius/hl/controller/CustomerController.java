package ru.sirius.hl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sirius.hl.dto.CustomerDto;
import ru.sirius.hl.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Tag(name = "Customer Controller")
public class CustomerController {

    private final CustomerService customerService;


    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAll() {
        customerService.clearAll();
        return ResponseEntity.ok("All customers and related tickets cleared");
    }

    @GetMapping
    @Operation(summary = "Получение пользователей `без пагинации")
    public List<CustomerDto> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя")
    public CustomerDto getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя физическое")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @PostMapping
    @Operation(summary = "Создание пользователя")
    public CustomerDto saveCustomer(@RequestBody CustomerDto customer) {
        return customerService.saveCustomer(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение пользователя")
    public CustomerDto updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerDto customer) {
        return customerService.updateCustomer(id, customer);
    }

}
