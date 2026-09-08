package dao;

import modelo.DetalleVenta;
import java.sql.*;

public class DetalleVentaDAO {
    Conexion conexion = new Conexion();

    // Insertar detalle de venta
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
}
