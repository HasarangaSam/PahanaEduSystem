package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Singleton class to manage database connections.
 */
public class DBConnection {

    private static Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/pahanaedudb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private DBConnection() {}

    /**
     * Returns a single, valid DB connection instance.
     */
    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}