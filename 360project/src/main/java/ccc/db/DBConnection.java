package ccc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    private static final String url = "jdbc:mysql://localhost";
    private static final String databaseName = "project_hy360_database";
    private static final int port = 3306;
    private static final String username = "root";
    private static final String password = "";

    /**
     * Attempts to establish a database connection
     *
     * @return a connection to the database
     * @throws SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        //Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(url + ":" + port + "/" + databaseName, username, password);
    }

    public static Connection getInitialConnection() throws SQLException, ClassNotFoundException {
        // Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(url + ":" + port, username, password);
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

}
