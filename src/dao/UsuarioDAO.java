package dao;

import modelo.Usuario;
import seguridad.PasswordUtil;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    Conexion conexion = new Conexion();

    // Validar login
    public Usuario validarLogin(String correo, String contraseña) {
        if (correo == null || contraseña == null) {
            return null;
        }

        String sql = "SELECT id_usuario, nombre, correo, contraseña, rol FROM usuarios WHERE correo=?";
        try (Connection con = conexion.getConnection()) {
            if (con == null) {
                return null;
            }

            String contraseñaGuardada;
            Usuario usuario;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, correo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }
                    contraseñaGuardada = rs.getString("contraseña");
                    boolean válida = PasswordUtil.isEncoded(contraseñaGuardada)
                            ? PasswordUtil.verify(contraseña, contraseñaGuardada)
                            : contraseñaGuardada != null && MessageDigest.isEqual(
                                    contraseña.getBytes(StandardCharsets.UTF_8),
                                    contraseñaGuardada.getBytes(StandardCharsets.UTF_8));
                    if (!válida) {
                        return null;
                    }
                    usuario = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            null,
                            rs.getString("rol"));
                }
            }

            if (!PasswordUtil.isEncoded(contraseñaGuardada)) {
                try (PreparedStatement actualizar = con.prepareStatement(
                        "UPDATE usuarios SET contraseña=? WHERE id_usuario=?")) {
                    actualizar.setString(1, PasswordUtil.hash(contraseña));
                    actualizar.setInt(2, usuario.getIdUsuario());
                    actualizar.executeUpdate();
                }
            }
            return usuario;
        } catch (Exception e) {
            System.out.println("Error en validarLogin: " + e.getMessage());
        }
        return null;
    }

    public boolean existeCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT 1 FROM usuarios WHERE correo=?";
        try (Connection con = conexion.getConnection()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, correo.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Error en existeCorreo: " + e.getMessage());
            return false;
        }
    }

    // Insertar nuevo usuario
    public boolean insertarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getContraseña() == null
            || !("admin".equalsIgnoreCase(usuario.getRol())
                || "vendedor".equalsIgnoreCase(usuario.getRol()))) {
            return false;
        }
        String sql = "INSERT INTO usuarios (nombre, correo, contraseña, rol) VALUES (?,?,?,?)";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, PasswordUtil.hash(usuario.getContraseña()));
            ps.setString(4, usuario.getRol());
            ps.executeUpdate();
            System.out.println("Usuario insertado correctamente.");
            return true;
        } catch (Exception e) {
            System.out.println("Error en insertarUsuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, correo, rol FROM usuarios ORDER BY nombre";
        try (Connection con = conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    null,
                    rs.getString("rol")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error en listarUsuarios: " + e.getMessage());
        }
        return lista;
    }
}
