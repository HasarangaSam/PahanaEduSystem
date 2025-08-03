package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import util.PasswordUtil;

import java.io.IOException;

/**
 * RegisterAdminServlet - Handles temporary admin registration.
 */
@WebServlet("/register-admin")
public class RegisterAdminServlet extends HttpServlet {

    // Handles POST request for admin registration
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get username and password from form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Hash the password using SHA-256
        String hashedPassword = PasswordUtil.hashPassword(password);

        // Create a new admin user
        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(hashedPassword);
        admin.setRole("admin");

        // Insert into DB using DAO
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.addUser(admin);

        // Show message
        if (success) {
            request.setAttribute("success", "Admin registered successfully.");
        } else {
            request.setAttribute("error", "Registration failed. Username might be taken.");
        }

        // Reload the page with message
        request.getRequestDispatcher("register_admin.jsp").forward(request, response);
    }
}
