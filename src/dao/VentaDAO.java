package dao;

import modelo.Venta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    Conexion conexion = new Conexion();

    public int insertarVenta(Venta venta) {
        int idGenerado = -1;
        // Obtiene el ID para relacionar después el detalle de la venta.
        String sql = "INSERT INTO ventas (fecha, id_cliente, id_usuario, total) VALUES (?,?,?,?)";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(venta.getFecha().getTime()));
            ps.setInt(2, venta.getIdCliente());
            ps.setInt(3, venta.getIdUsuario());
            ps.setDouble(4, venta.getTotal());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }
            System.out.println("Venta insertada correctamente.");
        } catch (Exception e) {
            System.out.println("Error en insertarVenta: " + e.getMessage());
        }
        return idGenerado;
    }

    public boolean actualizarVenta(int idVenta, int idCliente, int idUsuario, double total) {
        String sql = "UPDATE ventas SET id_cliente=?, id_usuario=?, total=? WHERE id_venta=?";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setInt(2, idUsuario);
            ps.setDouble(3, total);
            ps.setInt(4, idVenta);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error en actualizarVenta: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarVenta(int idVenta) {
        String sql = "DELETE FROM ventas WHERE id_venta=?";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error en eliminarVenta: " + e.getMessage());
            return false;
        }
    }

    public List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY fecha DESC";
        try (Connection con = conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Venta venta = new Venta(
                    rs.getInt("id_venta"),
                    rs.getTimestamp("fecha"),
                    rs.getInt("id_cliente"),
                    rs.getInt("id_usuario"),
                    rs.getDouble("total")
                );
                lista.add(venta);
            }
        } catch (Exception e) {
            System.out.println("Error en listarVentas: " + e.getMessage());
        }
        return lista;
    }
}
