package servlets;

import dao.ProductoDAO;
import modelo.Producto;
import modelo.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ProductoServlet")
public class ProductoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Usuario obtenerUsuarioLogueado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Usuario) session.getAttribute("usuario");
    }

    private boolean requiereLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = obtenerUsuarioLogueado(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp?error=login_required");
            return true;
        }
        return false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requiereLogin(request, response)) {
            return;
        }
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> productos = productoDAO.listarProductos();
        request.setAttribute("productos", productos);
        request.setAttribute("usuario", obtenerUsuarioLogueado(request));
        request.getRequestDispatcher("/jsp/productos.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requiereLogin(request, response)) {
            return;
        }

        Usuario usuario = obtenerUsuarioLogueado(request);
        if (!"admin".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?error=permiso");
            return;
        }

        String accion = request.getParameter("accion");
        ProductoDAO productoDAO = new ProductoDAO();

        if ("eliminar".equals(accion)) {
            String idParam = request.getParameter("id_producto");
            try {
                int idProducto = Integer.parseInt(idParam);
                productoDAO.eliminarProducto(idProducto);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/ProductoServlet?error=datos");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/ProductoServlet");
            return;
        }

        if ("actualizar".equals(accion)) {
            String idParam = request.getParameter("id_producto");
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String precioParam = request.getParameter("precio");
            String stockParam = request.getParameter("stock");

            try {
                int idProducto = Integer.parseInt(idParam);
                double precio = Double.parseDouble(precioParam);
                int stock = Integer.parseInt(stockParam);
                if (nombre == null || nombre.trim().isEmpty() || precio < 0 || stock < 0) {
                    response.sendRedirect(request.getContextPath() + "/ProductoServlet?error=datos");
                    return;
                }
                productoDAO.actualizarProducto(idProducto, nombre.trim(), descripcion, precio, stock);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/ProductoServlet?error=datos");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/ProductoServlet");
            return;
        }

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        double precio;
        int stock;
        try {
            precio = Double.parseDouble(request.getParameter("precio"));
            stock = Integer.parseInt(request.getParameter("stock"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?error=datos");
            return;
        }

        if (nombre == null || nombre.trim().isEmpty() || precio < 0 || stock < 0) {
            response.sendRedirect(request.getContextPath() + "/ProductoServlet?error=datos");
            return;
        }

        Producto nuevo = new Producto(0, nombre.trim(), descripcion, precio, stock);
        productoDAO.insertarProducto(nuevo);
        response.sendRedirect(request.getContextPath() + "/ProductoServlet");
    }
}
