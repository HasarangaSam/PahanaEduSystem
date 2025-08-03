package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;

import util.DBConnection;
import util.PasswordUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/adminRegister")
public class AdminRegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String plainPassword = request.getParameter("password");

        // Fixed role = "admin"
        String role = "admin";

        // Hash the password before storing
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, role);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                request.setAttribute("message", "Admin registered successfully!");
            } else {
                request.setAttribute("message", "Registration failed. Try again.");
            }
        } catch (Exception e) {
            request.setAttribute("message", "Error: " + e.getMessage());
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("adminRegister.jsp");
        dispatcher.forward(request, response);
    }
}
