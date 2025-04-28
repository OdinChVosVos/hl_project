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
    private final ObservabilityService observabilityService;

    @Transactional
    public void clearAll() {
        long startTime = observabilityService.startTiming();
        try {
            ticketService.clearAll(); // Clear dependent tickets first
            customerRepository.deleteAll();
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public List<CustomerDto> getAllCustomers() {
        long startTime = observabilityService.startTiming();
        try {
            List<Customer> customers = customerRepository.findAll();
            return customers.stream()
                    .map(customer -> modelMapper.map(customer, CustomerDto.class))
                    .collect(Collectors.toList());
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public CustomerDto getCustomerById(Long id) {
        long startTime = observabilityService.startTiming();
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Customer with ID " + id + " not found"));
            return modelMapper.map(customer, CustomerDto.class);
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public void deleteCustomer(Long id) {
        long startTime = observabilityService.startTiming();
        try {
            customerRepository.deleteById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format: " + id);
        } catch (Exception e) {
            throw new NoSuchElementException("Customer with ID " + id + " not found");
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public CustomerDto saveCustomer(CustomerDto customer) {
        long startTime = observabilityService.startTiming();
        try {
            Customer savedCustomer = customerRepository.save(
                    modelMapper.map(customer, Customer.class));
            return modelMapper.map(savedCustomer, CustomerDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Invalid email format: " + customer.getEmail(), e);
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public CustomerDto updateCustomer(Long id, CustomerDto updatedCustomer) {
        long startTime = observabilityService.startTiming();
        try {
            Customer existingCustomer = customerRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Customer with ID " + id + " not found"));

            // Update fields
            BeanUtils.copyProperties(updatedCustomer, existingCustomer, getNullPropertyNames(updatedCustomer));

            Customer savedCustomer = customerRepository.save(existingCustomer);
            return modelMapper.map(savedCustomer, CustomerDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update customer with ID " + id, e);
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }
}