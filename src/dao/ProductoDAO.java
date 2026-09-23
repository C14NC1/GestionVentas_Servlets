package dao;

import modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    Conexion conexion = new Conexion();

    public void insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock) VALUES (?,?,?,?)";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.executeUpdate();
            System.out.println("Producto insertado correctamente.");
        } catch (Exception e) {
            System.out.println("Error en insertarProducto: " + e.getMessage());
        }
    }

    public boolean actualizarProducto(int idProducto, String nombre, String descripcion, double precio, int stock) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=? WHERE id_producto=?";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);
            ps.setInt(4, stock);
            ps.setInt(5, idProducto);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error en actualizarProducto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto=?";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error en eliminarProducto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE stock > 0 ORDER BY nombre";
        try (Connection con = conexion.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                );
                lista.add(producto);
            }
        } catch (Exception e) {
            System.out.println("Error en listarProductos: " + e.getMessage());
        }
        return lista;
    }
}
