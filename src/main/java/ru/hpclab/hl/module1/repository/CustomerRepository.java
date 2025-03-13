package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
