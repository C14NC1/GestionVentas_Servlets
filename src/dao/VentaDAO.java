package dao;

import modelo.Venta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    Conexion conexion = new Conexion();

    // Insertar venta y devolver ID generado
    public int insertarVenta(Venta venta) {
        int idGenerado = -1;
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

    // Listar ventas
    public List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas";
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
