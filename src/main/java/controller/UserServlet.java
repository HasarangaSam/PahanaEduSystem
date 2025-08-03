package controller;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
        "/admin/users",
        "/admin/add_user",
        "/admin/edit_user",
        "/admin/delete_user"
})
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        // Initialize DAO
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/admin/users".equals(path)) {
            // List users
            try {
                List<User> userList = userDAO.getAllUsers();
                request.setAttribute("users", userList);
                request.getRequestDispatcher("/admin/manage_users.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("../error.jsp");
            }

        } else if ("/admin/edit_user".equals(path)) {
            // Edit user form
            try {
                int userId = Integer.parseInt(request.getParameter("id"));
                User user = userDAO.getUserById(userId);
                request.setAttribute("user", user);
                request.getRequestDispatcher("/admin/edit_user.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("../error.jsp");
            }

        } else if ("/admin/delete_user".equals(path)) {
            // Delete user
            try {
                int userId = Integer.parseInt(request.getParameter("id"));
                userDAO.deleteUser(userId);
                response.sendRedirect("users");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("../error.jsp");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/admin/add_user".equals(path)) {
            // Add user
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            String hashedPassword = PasswordUtil.hashPassword(password); // Hashing the password

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(hashedPassword);
            newUser.setRole(role);

            try {
                userDAO.addUser(newUser);
                response.sendRedirect("users");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("../error.jsp");
            }

        } else if ("/admin/edit_user".equals(path)) {
            // Update user
            try {
                int userId = Integer.parseInt(request.getParameter("id"));
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String role = request.getParameter("role");

                User updatedUser = new User();
                updatedUser.setUserId(userId);
                updatedUser.setUsername(username);
                updatedUser.setRole(role);

                // If password provided, hash and set it
                if (password != null && !password.trim().isEmpty()) {
                    updatedUser.setPassword(PasswordUtil.hashPassword(password));
                } else {
                    updatedUser.setPassword(null); // Leave it null to skip updating password
                }

                userDAO.updateUser(updatedUser);
                response.sendRedirect("users");

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("../error.jsp");
            }
        }
    }
}
