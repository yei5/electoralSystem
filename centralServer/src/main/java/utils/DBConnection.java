package utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/votaciones";
    private static final String USER = "tu_usuario";
    private static final String PASSWORD = "tu_contraseña";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
