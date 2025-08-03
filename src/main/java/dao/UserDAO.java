package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;
import util.DBConnection;
import util.PasswordUtil;

/**
 * UserDAO - Handles database operations related to users.
 */
public class UserDAO {

    // Validates login by username and password
    public User validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                // Compares a plain password with the stored hashed password
                if (PasswordUtil.checkPassword(password, storedHashedPassword)) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(storedHashedPassword);
                    user.setRole(rs.getString("role"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Returns all users except admin
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role IN ('cashier', 'storekeeper')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log error
        }

        return users;
    }

    // Adds a new user (cashier or storekeeper)
    public void addUser(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // Already hashed
            stmt.setString(3, user.getRole());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(); // Log error
        }
    }

    // Finds a user by ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                // We do not set the password field here
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    // Updates a user - password only updated if new password is provided
    public void updateUser(User user) {
        String sqlWithPassword = "UPDATE users SET username = ?, password = ?, role = ? WHERE user_id = ?";
        String sqlWithoutPassword = "UPDATE users SET username = ?, role = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                // If password provided, update it (already hashed)
                try (PreparedStatement stmt = conn.prepareStatement(sqlWithPassword)) {
                    stmt.setString(1, user.getUsername());
                    stmt.setString(2, user.getPassword());
                    stmt.setString(3, user.getRole());
                    stmt.setInt(4, user.getUserId());
                    stmt.executeUpdate();
                }
            } else {
                // If password blank, keep old password
                try (PreparedStatement stmt = conn.prepareStatement(sqlWithoutPassword)) {
                    stmt.setString(1, user.getUsername());
                    stmt.setString(2, user.getRole());
                    stmt.setInt(3, user.getUserId());
                    stmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log error
        }
    }

    // Deletes a user by ID
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(); // Log error
        }
    }
}
