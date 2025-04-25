package ru.sirius.hl.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.sirius.hl.dto.CustomerDto;
import ru.sirius.hl.model.Customer;
import ru.sirius.hl.repository.CustomerRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.sirius.hl.service.BeanUtilsHelper.getNullPropertyNames;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final TicketService ticketService;


    @Transactional
    public void clearAll() {
        ticketService.clearAll();  // Clear dependent tickets first
        customerRepository.deleteAll();
    }


    // Get all customers (no pagination)
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }

    // Get customer by ID
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + id + " not found"));

        return modelMapper.map(customer, CustomerDto.class);
    }

    // Delete customer by ID (physical deletion)
    public void deleteCustomer(Long id) {
        try {
            customerRepository.deleteById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format: " + id);
        } catch (Exception e) {
            throw new NoSuchElementException("Customer with ID " + id + " not found");
        }
    }

    // Save a new customer
    public CustomerDto saveCustomer(CustomerDto customer) {
        try {
            Customer savedCustomer = customerRepository.save(
                    modelMapper.map(customer, Customer.class));

            return modelMapper.map(savedCustomer, CustomerDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Invalid email format: " + customer.getEmail(), e);
        }
    }

    // Update an existing customer
    public CustomerDto updateCustomer(Long id, CustomerDto updatedCustomer) {
        try{
            Customer existingCustomer = customerRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Customer with ID " + id + " not found"));

            // Update fields
            BeanUtils.copyProperties(updatedCustomer, existingCustomer, getNullPropertyNames(updatedCustomer));

            Customer savedCustomer = customerRepository.save(existingCustomer);
            return modelMapper.map(savedCustomer, CustomerDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update key with ID " + id, e);
        }

    }
}
