package test;

import dao.CustomerDAO;
import model.Customer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CustomerDAOTest {

    private CustomerDAO customerDAO;

    @Before
    public void setUp() {
        customerDAO = new CustomerDAO();
    }

    @Test
    public void testAddAndGetCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("JUnit");
        customer.setLastName("Test");
        customer.setAddress("123 Test St");
        customer.setTelephone("0771234567");
        customer.setEmail("junit@test.com");

        customerDAO.addCustomer(customer);

        List<Customer> customers = customerDAO.searchCustomersByFirstName("JUnit");
        assertFalse(customers.isEmpty());

        Customer retrieved = customers.get(0);
        assertEquals("JUnit", retrieved.getFirstName());
        assertEquals("Test", retrieved.getLastName());
    }

    @Test
    public void testUpdateCustomer() {
        List<Customer> customers = customerDAO.searchCustomersByFirstName("JUnit");
        if (!customers.isEmpty()) {
            Customer customer = customers.get(0);
            customer.setAddress("Updated Address");
            customerDAO.updateCustomer(customer);

            Customer updated = customerDAO.getCustomerById(customer.getCustomerId());
            assertEquals("Updated Address", updated.getAddress());
        }
    }

    @Test
    public void testDeleteCustomer() {
        List<Customer> customers = customerDAO.searchCustomersByFirstName("JUnit");
        if (!customers.isEmpty()) {
            int id = customers.get(0).getCustomerId();
            customerDAO.deleteCustomer(id);

            Customer deleted = customerDAO.getCustomerById(id);
            assertNull(deleted);
        }
    }
}
