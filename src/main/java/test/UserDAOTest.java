package test;

import dao.UserDAO;
import model.User;
import org.junit.Before;
import org.junit.Test;
import util.PasswordUtil;

import java.util.List;

import static org.junit.Assert.*;

public class UserDAOTest {

    private UserDAO userDAO;

    @Before
    public void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    public void testAddAndGetUserById() {
        // Create new user
        User user = new User();
        user.setUsername("testuser_junit");
        user.setPassword(PasswordUtil.hashPassword("test123")); // hash it before insert
        user.setRole("cashier");

        userDAO.addUser(user);

        // Fetch all users and pick last inserted one
        List<User> users = userDAO.getAllUsers();
        assertFalse(users.isEmpty());

        User latest = users.get(users.size() - 1);
        assertEquals("testuser_junit", latest.getUsername());
        assertEquals("cashier", latest.getRole());

        // Fetch by ID
        User fetched = userDAO.getUserById(latest.getUserId());
        assertNotNull(fetched);
        assertEquals("testuser_junit", fetched.getUsername());
    }

    @Test
    public void testValidateLogin() {
        // Add user first
        String plainPassword = "valid123";
        User user = new User();
        user.setUsername("login_user_junit");
        user.setPassword(PasswordUtil.hashPassword(plainPassword));
        user.setRole("storekeeper");

        userDAO.addUser(user);

        // Should pass login
        User validUser = userDAO.validateLogin("login_user_junit", plainPassword);
        assertNotNull(validUser);
        assertEquals("storekeeper", validUser.getRole());

        // Should fail login
        User invalidUser = userDAO.validateLogin("login_user_junit", "wrongpass");
        assertNull(invalidUser);
    }

    @Test
    public void testUpdateUserWithoutPassword() {
        // Add user
        User user = new User();
        user.setUsername("update_user_junit");
        user.setPassword(PasswordUtil.hashPassword("oldpass"));
        user.setRole("cashier");
        userDAO.addUser(user);

        // Get last user
        List<User> users = userDAO.getAllUsers();
        User last = users.get(users.size() - 1);

        // Update username and role only
        last.setUsername("updated_username");
        last.setRole("storekeeper");
        last.setPassword(""); // skip password update

        userDAO.updateUser(last);

        User updated = userDAO.getUserById(last.getUserId());
        assertEquals("updated_username", updated.getUsername());
        assertEquals("storekeeper", updated.getRole());
    }

    @Test
    public void testUpdateUserWithPassword() {
        // Add user
        User user = new User();
        user.setUsername("pwd_user_junit");
        user.setPassword(PasswordUtil.hashPassword("initial123"));
        user.setRole("cashier");
        userDAO.addUser(user);

        // Get last user
        List<User> users = userDAO.getAllUsers();
        User last = users.get(users.size() - 1);

        // Update with new password
        last.setUsername("pwd_updated");
        last.setPassword(PasswordUtil.hashPassword("newpass456"));
        last.setRole("cashier");

        userDAO.updateUser(last);

        // Login test
        User loggedIn = userDAO.validateLogin("pwd_updated", "newpass456");
        assertNotNull(loggedIn);
        assertEquals("cashier", loggedIn.getRole());
    }

    @Test
    public void testDeleteUser() {
        // Add user
        User user = new User();
        user.setUsername("delete_user_junit");
        user.setPassword(PasswordUtil.hashPassword("del123"));
        user.setRole("cashier");
        userDAO.addUser(user);

        // Get last user
        List<User> users = userDAO.getAllUsers();
        User last = users.get(users.size() - 1);

        userDAO.deleteUser(last.getUserId());

        // Should return null now
        User deleted = userDAO.getUserById(last.getUserId());
        assertNull(deleted);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userDAO.getAllUsers();
        assertNotNull(users);
        for (User user : users) {
            assertTrue(user.getRole().equals("cashier") || user.getRole().equals("storekeeper"));
        }
    }
}

