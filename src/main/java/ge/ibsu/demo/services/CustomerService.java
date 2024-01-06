package ge.ibsu.demo.services;

import ge.ibsu.demo.dto.AddCustomer;
import ge.ibsu.demo.entities.Address;
import ge.ibsu.demo.entities.Customer;
import ge.ibsu.demo.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressService addressService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressService addressService) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("CUSTOMER_NOT_FOUND"));
    }

    @Transactional
    public Customer addEditCustomer(AddCustomer addCustomer, Long id) {
        Customer customer = new Customer();
        if (id != null) {
            customer = getById(id);
        }

        customer.setFirstName(addCustomer.getFirstName());
        customer.setLastName(addCustomer.getLastName());
        if (id == null) {
            customer.setCreateDate(new Date());
        }

        Address address = addressService.getAddress(addCustomer.getAddress());
        customer.setAddress(address);

        return customerRepository.save(customer);
    }

    @Transactional
    public Boolean deleteCustomer(Long id) {
        Customer customer = getById(id);
        customerRepository.delete(customer);
        return true;
    }

}
