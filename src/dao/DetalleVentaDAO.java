package dao;

import modelo.DetalleVenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {
    Conexion conexion = new Conexion();

    public void insertarDetalle(DetalleVenta detalle) {
        String sql = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?,?,?,?,?)";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detalle.getIdVenta());
            ps.setInt(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecioUnitario());
            ps.setDouble(5, detalle.getSubtotal());
            ps.executeUpdate();
            System.out.println("Detalle de venta insertado correctamente.");
        } catch (Exception e) {
            System.out.println("Error en insertarDetalle: " + e.getMessage());
        }
    }

    public boolean actualizarDetalle(int idVenta, int idProducto, int cantidad, double precioUnitario, double subtotal) {
        String sql = "UPDATE detalle_venta SET id_producto=?, cantidad=?, precio_unitario=?, subtotal=? WHERE id_venta=?";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.setInt(2, cantidad);
            ps.setDouble(3, precioUnitario);
            ps.setDouble(4, subtotal);
            ps.setInt(5, idVenta);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error en actualizarDetalle: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarDetallePorVenta(int idVenta) {
        String sql = "DELETE FROM detalle_venta WHERE id_venta=?";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error en eliminarDetallePorVenta: " + e.getMessage());
            return false;
        }
    }

    public List<DetalleVenta> listarDetalles() {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_venta";
        try (Connection con = conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DetalleVenta(
                    rs.getInt("id_detalle"),
                    rs.getInt("id_venta"),
                    rs.getInt("id_producto"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getDouble("subtotal")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error en listarDetalles: " + e.getMessage());
        }
        return lista;
    }
}
