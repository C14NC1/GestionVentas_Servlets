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
        HttpSession session = request.getSession(false);
        modelo.Usuario usuario = (modelo.Usuario) session.getAttribute("usuario");
        VentaDAO ventaDAO = new VentaDAO();

        if ("eliminar".equals(accion)) {
            if (!"admin".equalsIgnoreCase(usuario.getRol())) {
                response.sendRedirect(request.getContextPath() + "/VentaServlet?error=permiso");
                return;
            }
            try {
                int idVenta = Integer.parseInt(request.getParameter("id_venta"));
                if (!ventaDAO.eliminarVentaCompleta(idVenta)) {
                    response.sendRedirect(request.getContextPath() + "/VentaServlet?error=guardar");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/VentaServlet");
            return;
        }

        if ("actualizar".equals(accion)) {
            if (!"admin".equalsIgnoreCase(usuario.getRol())) {
                response.sendRedirect(request.getContextPath() + "/VentaServlet?error=permiso");
                return;
            }
            try {
                int idVenta = Integer.parseInt(request.getParameter("id_venta"));
                int idCliente = Integer.parseInt(request.getParameter("id_cliente"));
                int idProducto = Integer.parseInt(request.getParameter("id_producto"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));

                if (idCliente <= 0 || idProducto <= 0 || cantidad <= 0) {
                    response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
                    return;
                }

                if (!ventaDAO.actualizarVentaConDetalle(idVenta, idCliente, idProducto, cantidad)) {
                    response.sendRedirect(request.getContextPath() + "/VentaServlet?error=guardar");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/VentaServlet");
            return;
        }

        int idCliente;
        int idProducto;
        int cantidad;
        try {
            idCliente = Integer.parseInt(request.getParameter("id_cliente"));
            idProducto = Integer.parseInt(request.getParameter("id_producto"));
            cantidad = Integer.parseInt(request.getParameter("cantidad"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
            return;
        }

        if (idCliente <= 0 || idProducto <= 0 || cantidad <= 0) {
            response.sendRedirect(request.getContextPath() + "/VentaServlet?error=datos");
            return;
        }

        int idVentaGenerado = ventaDAO.registrarVenta(
            idCliente, usuario.getIdUsuario(), idProducto, cantidad);

        if (idVentaGenerado <= 0) {
            response.sendRedirect(request.getContextPath() + "/VentaServlet?error=guardar");
            return;
        }

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
