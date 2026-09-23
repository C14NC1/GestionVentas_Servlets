package servlets;

import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import dao.DetalleVentaDAO;
import dao.UsuarioDAO;
import modelo.Venta;
import modelo.DetalleVenta;
import modelo.Producto;
import modelo.Cliente;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/VentaServlet")
public class VentaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        VentaDAO ventaDAO = new VentaDAO();
        DetalleVentaDAO detalleDAO = new DetalleVentaDAO();

        if ("eliminar".equals(accion)) {
            try {
                int idVenta = Integer.parseInt(request.getParameter("id_venta"));
                detalleDAO.eliminarDetallePorVenta(idVenta);
                ventaDAO.eliminarVenta(idVenta);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/VentaServlet");
            return;
        }

        if ("actualizar".equals(accion)) {
            try {
                int idVenta = Integer.parseInt(request.getParameter("id_venta"));
                int idCliente = Integer.parseInt(request.getParameter("id_cliente"));
                int idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
                int idProducto = Integer.parseInt(request.getParameter("id_producto"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                double precioUnitario = Double.parseDouble(request.getParameter("precio_unitario"));

                if (idCliente <= 0 || idUsuario <= 0 || idProducto <= 0 || cantidad <= 0 || precioUnitario <= 0) {
                    response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
                    return;
                }

                double subtotal = cantidad * precioUnitario;
                ventaDAO.actualizarVenta(idVenta, idCliente, idUsuario, subtotal);
                detalleDAO.actualizarDetalle(idVenta, idProducto, cantidad, precioUnitario, subtotal);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/VentaServlet");
            return;
        }

        int idCliente;
        int idUsuario;
        int idProducto;
        int cantidad;
        double precioUnitario;
        try {
            idCliente = Integer.parseInt(request.getParameter("id_cliente"));
            idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
            idProducto = Integer.parseInt(request.getParameter("id_producto"));
            cantidad = Integer.parseInt(request.getParameter("cantidad"));
            precioUnitario = Double.parseDouble(request.getParameter("precio_unitario"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
            return;
        }

        if (idCliente <= 0 || idUsuario <= 0 || idProducto <= 0 || cantidad <= 0 || precioUnitario <= 0) {
            response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
            return;
        }

        double total = cantidad * precioUnitario;
        Venta nuevaVenta = new Venta(0, new Date(), idCliente, idUsuario, total);
        int idVentaGenerado = ventaDAO.insertarVenta(nuevaVenta);

        if (idVentaGenerado <= 0) {
            response.sendRedirect(request.getContextPath() + "/VentaServlet?error=guardar");
            return;
        }

        // Guarda el producto vendido como detalle de la venta creada.
        DetalleVenta detalle = new DetalleVenta(0, idVentaGenerado, idProducto, cantidad, precioUnitario, total);
        detalleDAO.insertarDetalle(detalle);

        response.sendRedirect(request.getContextPath() + "/VentaServlet?success=1");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VentaDAO ventaDAO = new VentaDAO();
        ProductoDAO productoDAO = new ProductoDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        List<Venta> ventas = ventaDAO.listarVentas();
        List<Producto> productos = productoDAO.listarProductos();
        List<Cliente> clientes = clienteDAO.listarClientes();
        List<DetalleVenta> detalles = detalleDAO.listarDetalles();
        List<modelo.Usuario> usuarios = usuarioDAO.listarUsuarios();

        request.setAttribute("ventas", ventas);
        request.setAttribute("productos", productos);
        request.setAttribute("clientes", clientes);
        request.setAttribute("detalles", detalles);
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/jsp/ventas.jsp").forward(request, response);
    }
}
