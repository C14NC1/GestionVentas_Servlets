package dao;

import modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    Conexion conexion = new Conexion();

    public void insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, correo, telefono, direccion) VALUES (?,?,?,?)";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getCorreo());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getDireccion());
            ps.executeUpdate();
            System.out.println("Cliente insertado correctamente.");
        } catch (Exception e) {
            System.out.println("Error en insertarCliente: " + e.getMessage());
        }
    }

    public boolean eliminarCliente(int idCliente) {
        String sql = "DELETE FROM clientes WHERE id_cliente=?";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error en eliminarCliente: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre";
        try (Connection con = conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("telefono"),
                    rs.getString("direccion")
                );
                lista.add(cliente);
            }
        } catch (Exception e) {
            System.out.println("Error en listarClientes: " + e.getMessage());
        }
        return lista;
    }
}
