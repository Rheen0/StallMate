package src.main.java.com.puplagoon.pos.model.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private static Connection connection = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection != null)
            return connection;

        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find db.properties");
            }
            Properties prop = new Properties();
            prop.load(input);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("Database connection properties are not set properly");
            }
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully");
        } catch (Exception e) {
            System.err.println("Connection failed");
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("Connection closed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
