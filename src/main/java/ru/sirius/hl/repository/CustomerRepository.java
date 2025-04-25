package ru.sirius.hl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sirius.hl.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
