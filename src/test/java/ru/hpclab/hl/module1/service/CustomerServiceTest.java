//package ru.hpclab.hl.module1.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import ru.hpclab.hl.module1.model.Customer;
//import ru.hpclab.hl.module1.repository.CustomerRepository;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {CustomerServiceTest.UserServiceTestConfiguration.class})
//@RequiredArgs
//public class CustomerServiceTest {
//    private CustomerService customerService;
//    private CustomerRepository customerRepository;
//
//
//
//    @Test
//    public void testCreateAndGet(){
//        //create
//        Customer customer = new Customer(UUID.randomUUID(), "name");
//
//        Customer savedCustomer = customerService.saveUser(customer);
//
//        Assertions.assertEquals(customer.getFio(), savedCustomer.getFio());
//        Mockito.verify(userRepository, Mockito.times(1)).save(customer);
//
//        //getAll
//        List<Customer> customerList = customerService.getAllUsers();
//
//        Assertions.assertEquals("name1", customerList.get(0).getFio());
//        Assertions.assertEquals("name2", customerList.get(1).getFio());
//        Mockito.verify(userRepository, Mockito.times(1)).findAll();
//
//    }
//
//    @Configuration
//    static class UserServiceTestConfiguration {
//
//        @Bean
//        UserRepository userRepository() {
//            UserRepository userRepository = mock(UserRepository.class);
//            when(userRepository.save(any())).thenReturn(new Customer(UUID.randomUUID(), "name"));
//            when(userRepository.findAll())
//                    .thenReturn(Arrays.asList(new Customer(UUID.randomUUID(), "name1"),
//                            new Customer(UUID.randomUUID(), "name2")));
//            return userRepository;
//        }
//
//        @Bean
//        CustomerService UserService(UserRepository userRepository){
//            return new CustomerService(userRepository);
//        }
//    }
//
//}
