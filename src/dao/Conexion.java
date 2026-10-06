package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    private static final String URL = System.getenv().getOrDefault(
            "GESTION_DB_URL", "jdbc:mysql://localhost:3306/gestion_ventas");
    private static final String USER = System.getenv().getOrDefault("GESTION_DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("GESTION_DB_PASSWORD", "");

    public Connection getConnection() {
        Connection con = null;
        try {
            // Cargar el controlador y abrir la conexión
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (Exception e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
        return con;
    }
}
