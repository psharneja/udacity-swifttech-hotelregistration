package services;

import models.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {

    private static CustomerService instance;

    private final Map<String, Customer> customers;

    private CustomerService() {
        this.customers = new HashMap<>();
    }

    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }

        return instance;
    }
    public void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(firstName, lastName, email);
        customers.put(email, newCustomer);
    }


    public Customer getCustomer(String customerEmail) {
        return this.customers.get(customerEmail);
    }


    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }


}
