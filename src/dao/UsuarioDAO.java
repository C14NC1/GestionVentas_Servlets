package dao;

import modelo.Usuario;
import java.sql.*;

public class UsuarioDAO {
    Conexion conexion = new Conexion();

    // Validar login
    public Usuario validarLogin(String correo, String contraseña) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE correo=? AND contraseña=?";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contraseña);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contraseña"),
                    rs.getString("rol")
                );
            }
        } catch (Exception e) {
            System.out.println("Error en validarLogin: " + e.getMessage());
        }
        return usuario;
    }

    // Insertar nuevo usuario
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, contraseña, rol) VALUES (?,?,?,?)";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContraseña());
            ps.setString(4, usuario.getRol());
            ps.executeUpdate();
            System.out.println("Usuario insertado correctamente.");
            return true;
        } catch (Exception e) {
            System.out.println("Error en insertarUsuario: " + e.getMessage());
            return false;
        }
    }
}
