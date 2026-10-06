package dao;

import modelo.Venta;
import java.sql.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VentaDAO {
    Conexion conexion = new Conexion();

    public int registrarVenta(int idCliente, int idUsuario, int idProducto, int cantidad) {
        if (idCliente <= 0 || idUsuario <= 0 || idProducto <= 0 || cantidad <= 0) {
            return -1;
        }

        try (Connection con = conexion.getConnection()) {
            if (con == null) {
                return -1;
            }
            con.setAutoCommit(false);
            try {
                BigDecimal precio;
                int stock;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT precio, stock FROM productos WHERE id_producto=? FOR UPDATE")) {
                    ps.setInt(1, idProducto);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return -1;
                        }
                        precio = rs.getBigDecimal("precio");
                        stock = rs.getInt("stock");
                    }
                }
                if (precio == null || stock < cantidad) {
                    con.rollback();
                    return -1;
                }

                BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE productos SET stock=stock-? WHERE id_producto=? AND stock>=?")) {
                    ps.setInt(1, cantidad);
                    ps.setInt(2, idProducto);
                    ps.setInt(3, cantidad);
                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return -1;
                    }
                }

                int idVenta;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO ventas (fecha,id_cliente,id_usuario,total) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                    ps.setInt(2, idCliente);
                    ps.setInt(3, idUsuario);
                    ps.setBigDecimal(4, subtotal);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            con.rollback();
                            return -1;
                        }
                        idVenta = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO detalle_venta (id_venta,id_producto,cantidad,precio_unitario,subtotal) VALUES (?,?,?,?,?)")) {
                    ps.setInt(1, idVenta);
                    ps.setInt(2, idProducto);
                    ps.setInt(3, cantidad);
                    ps.setBigDecimal(4, precio);
                    ps.setBigDecimal(5, subtotal);
                    ps.executeUpdate();
                }
                con.commit();
                return idVenta;
            } catch (Exception e) {
                con.rollback();
                System.out.println("Error en registrarVenta: " + e.getMessage());
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Error en registrarVenta: " + e.getMessage());
            return -1;
        }
    }

    public boolean actualizarVentaConDetalle(int idVenta, int idCliente, int idProducto, int cantidad) {
        if (idVenta <= 0 || idCliente <= 0 || idProducto <= 0 || cantidad <= 0) {
            return false;
        }

        try (Connection con = conexion.getConnection()) {
            if (con == null) {
                return false;
            }
            con.setAutoCommit(false);
            try {
                int productoAnterior;
                int cantidadAnterior;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT id_producto,cantidad FROM detalle_venta WHERE id_venta=? FOR UPDATE")) {
                    ps.setInt(1, idVenta);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                        productoAnterior = rs.getInt("id_producto");
                        cantidadAnterior = rs.getInt("cantidad");
                    }
                }

                int[] idsProducto = productoAnterior == idProducto
                        ? new int[] { productoAnterior }
                        : new int[] { Math.min(productoAnterior, idProducto), Math.max(productoAnterior, idProducto) };
                Map<Integer, ProductoBloqueado> productos = new HashMap<>();
                for (int id : idsProducto) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "SELECT precio,stock FROM productos WHERE id_producto=? FOR UPDATE")) {
                        ps.setInt(1, id);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                con.rollback();
                                return false;
                            }
                            productos.put(id, new ProductoBloqueado(
                                    rs.getBigDecimal("precio"), rs.getInt("stock")));
                        }
                    }
                }

                ProductoBloqueado productoNuevo = productos.get(idProducto);
                int stockNuevo = productoAnterior == idProducto
                        ? productoNuevo.stock + cantidadAnterior - cantidad
                        : productoNuevo.stock - cantidad;
                if (productoNuevo.precio == null || stockNuevo < 0) {
                    con.rollback();
                    return false;
                }

                if (productoAnterior == idProducto) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE productos SET stock=? WHERE id_producto=?")) {
                        ps.setInt(1, stockNuevo);
                        ps.setInt(2, idProducto);
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE productos SET stock=stock+? WHERE id_producto=?")) {
                        ps.setInt(1, cantidadAnterior);
                        ps.setInt(2, productoAnterior);
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE productos SET stock=stock-? WHERE id_producto=?")) {
                        ps.setInt(1, cantidad);
                        ps.setInt(2, idProducto);
                        ps.executeUpdate();
                    }
                }

                BigDecimal subtotal = productoNuevo.precio.multiply(BigDecimal.valueOf(cantidad));
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE ventas SET id_cliente=?,total=? WHERE id_venta=?")) {
                    ps.setInt(1, idCliente);
                    ps.setBigDecimal(2, subtotal);
                    ps.setInt(3, idVenta);
                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return false;
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE detalle_venta SET id_producto=?,cantidad=?,precio_unitario=?,subtotal=? WHERE id_venta=?")) {
                    ps.setInt(1, idProducto);
                    ps.setInt(2, cantidad);
                    ps.setBigDecimal(3, productoNuevo.precio);
                    ps.setBigDecimal(4, subtotal);
                    ps.setInt(5, idVenta);
                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return false;
                    }
                }
                con.commit();
                return true;
            } catch (Exception e) {
                con.rollback();
                System.out.println("Error en actualizarVentaConDetalle: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error en actualizarVentaConDetalle: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarVentaCompleta(int idVenta) {
        if (idVenta <= 0) {
            return false;
        }

        try (Connection con = conexion.getConnection()) {
            if (con == null) {
                return false;
            }
            con.setAutoCommit(false);
            try {
                int idProducto = 0;
                int cantidad = 0;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT id_producto,cantidad FROM detalle_venta WHERE id_venta=? FOR UPDATE")) {
                    ps.setInt(1, idVenta);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            idProducto = rs.getInt("id_producto");
                            cantidad = rs.getInt("cantidad");
                        }
                    }
                }
                if (cantidad > 0) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE productos SET stock=stock+? WHERE id_producto=?")) {
                        ps.setInt(1, cantidad);
                        ps.setInt(2, idProducto);
                        ps.executeUpdate();
                    }
                }
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM detalle_venta WHERE id_venta=?")) {
                    ps.setInt(1, idVenta);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM ventas WHERE id_venta=?")) {
                    ps.setInt(1, idVenta);
                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return false;
                    }
                }
                con.commit();
                return true;
            } catch (Exception e) {
                con.rollback();
                System.out.println("Error en eliminarVentaCompleta: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error en eliminarVentaCompleta: " + e.getMessage());
            return false;
        }
    }

    private static class ProductoBloqueado {
        private final BigDecimal precio;
        private final int stock;

        private ProductoBloqueado(BigDecimal precio, int stock) {
            this.precio = precio;
            this.stock = stock;
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
