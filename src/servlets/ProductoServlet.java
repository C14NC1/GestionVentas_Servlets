package servlets;

import dao.ProductoDAO;
import modelo.Producto;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ProductoServlet")
public class ProductoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> productos = productoDAO.listarProductos();
        request.setAttribute("productos", productos);
        request.getRequestDispatcher("/jsp/productos.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        double precio;
        int stock;
        try {
            precio = Double.parseDouble(request.getParameter("precio"));
            stock = Integer.parseInt(request.getParameter("stock"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/jsp/productos.jsp?error=datos");
            return;
        }

        if (nombre == null || nombre.trim().isEmpty() || precio < 0 || stock < 0) {
            response.sendRedirect(request.getContextPath() + "/jsp/productos.jsp?error=datos");
            return;
        }

        Producto nuevo = new Producto(0, nombre.trim(), descripcion, precio, stock);
        ProductoDAO productoDAO = new ProductoDAO();
        productoDAO.insertarProducto(nuevo);

        response.sendRedirect(request.getContextPath() + "/ProductoServlet");
    }
}
