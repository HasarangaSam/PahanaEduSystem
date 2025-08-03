package controller;

import dao.CustomerDAO;
import model.Customer;
import util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * CustomerServlet - Handles add, edit, delete, and search operations for customers.
 */
@WebServlet(urlPatterns = {
    "/admin/customers", "/cashier/customers",             // View/search customers
    "/admin/add_customer", "/cashier/add_customer",       // Add customer (POST)
    "/admin/edit_customer", "/cashier/edit_customer",     // Edit customer (GET + POST)
    "/admin/delete_customer", "/cashier/delete_customer"  // Delete customer (GET)
})
public class CustomerServlet extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override
    public void init() {
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String role = ((model.User) request.getSession().getAttribute("user")).getRole();

        // ✅ View/Search customers
        if (path.endsWith("/customers")) {
            String search = request.getParameter("search");

            List<Customer> customerList = (search != null && !search.trim().isEmpty())
                    ? customerDAO.searchCustomersByFirstName(search)
                    : customerDAO.getAllCustomers();

            request.setAttribute("customers", customerList);

            // ✅ Forward based on role
            if ("cashier".equals(role)) {
                request.getRequestDispatcher("/cashier/manage_customers.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/admin/manage_customers.jsp").forward(request, response);
            }
        }

        // ✅ Show Edit Customer Form
        else if (path.endsWith("/edit_customer")) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Customer customer = customerDAO.getCustomerById(id);

                if (customer != null) {
                    request.setAttribute("customer", customer);
                    if ("cashier".equals(role)) {
                        request.getRequestDispatcher("/cashier/edit_customer.jsp").forward(request, response);
                    } else {
                        request.getRequestDispatcher("/admin/edit_customer.jsp").forward(request, response);
                    }
                } else {
                    response.sendRedirect(getRedirectPath(role, "customers?error=Customer+not+found"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(getRedirectPath(role, "customers?error=Invalid+customer+ID"));
            }
        }

        // ✅ Delete customer
        else if (path.endsWith("/delete_customer")) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                customerDAO.deleteCustomer(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.sendRedirect(getRedirectPath(role, "customers"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String role = ((model.User) request.getSession().getAttribute("user")).getRole();

        // ✅ Add Customer
        if (path.endsWith("/add_customer")) {
            Customer customer = extractCustomerFromRequest(request, false);

            if (customer != null) {
                customerDAO.addCustomer(customer);
                response.sendRedirect(getRedirectPath(role, "customers"));
            } else {
                response.sendRedirect(getRedirectPath(role, "add_customer.jsp?error=Invalid+input"));
            }
        }

        // ✅ Update Customer
        else if (path.endsWith("/edit_customer")) {
            Customer customer = extractCustomerFromRequest(request, true);

            if (customer != null) {
                customerDAO.updateCustomer(customer);
                response.sendRedirect(getRedirectPath(role, "customers"));
            } else {
                response.sendRedirect(getRedirectPath(role, "edit_customer?id=" + request.getParameter("id") + "&error=Invalid+input"));
            }
        }
    }

    // ✅ Helper method to extract form input into Customer object
    private Customer extractCustomerFromRequest(HttpServletRequest request, boolean includeId) {
        try {
            String firstName = request.getParameter("first_name");
            String lastName = request.getParameter("last_name");
            String address = request.getParameter("address");
            String phone = request.getParameter("telephone");
            String email = request.getParameter("email");

            if (!ValidationUtil.isValidName(firstName) || !ValidationUtil.isValidName(lastName)
                    || !ValidationUtil.isValidPhone(phone) || !ValidationUtil.isValidEmail(email)) {
                return null;
            }

            Customer customer = new Customer();
            customer.setFirstName(firstName.trim());
            customer.setLastName(lastName.trim());
            customer.setAddress(address.trim());
            customer.setTelephone(phone.trim());
            customer.setEmail(email.trim());

            if (includeId) {
                int id = Integer.parseInt(request.getParameter("id"));
                customer.setCustomerId(id);
            }

            return customer;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Helper to redirect based on role
    private String getRedirectPath(String role, String path) {
        return ("cashier".equals(role) ? "customers" : path);
    }
}
