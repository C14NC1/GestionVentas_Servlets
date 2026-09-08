package servlets;

import dao.VentaDAO;
import dao.DetalleVentaDAO;
import modelo.Venta;
import modelo.DetalleVenta;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/VentaServlet")
public class VentaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idCliente;
        int idUsuario;
        double total;
        int idProducto;
        int cantidad;
        double precioUnitario;
        try {
            idCliente = Integer.parseInt(request.getParameter("id_cliente"));
            idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
            total = Double.parseDouble(request.getParameter("total"));
            idProducto = Integer.parseInt(request.getParameter("id_producto"));
            cantidad = Integer.parseInt(request.getParameter("cantidad"));
            precioUnitario = Double.parseDouble(request.getParameter("precio_unitario"));
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/jsp/ventas.jsp?error=datos");
            return;
        }

        if (idCliente <= 0 || idUsuario <= 0 || idProducto <= 0 || cantidad <= 0
                || total < 0 || precioUnitario < 0) {
            response.sendRedirect(request.getContextPath() + "/jsp/ventas.jsp?error=datos");
            return;
        }

        Venta nuevaVenta = new Venta(0, new Date(), idCliente, idUsuario, total);
        VentaDAO ventaDAO = new VentaDAO();
        int idVentaGenerado = ventaDAO.insertarVenta(nuevaVenta);

        if (idVentaGenerado <= 0) {
            response.sendRedirect(request.getContextPath() + "/jsp/ventas.jsp?error=guardar");
            return;
        }

        double subtotal = cantidad * precioUnitario;

        DetalleVenta detalle = new DetalleVenta(0, idVentaGenerado, idProducto, cantidad, precioUnitario, subtotal);
        DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
        detalleDAO.insertarDetalle(detalle);

        response.sendRedirect(request.getContextPath() + "/VentaServlet?success=1");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VentaDAO ventaDAO = new VentaDAO();
        request.setAttribute("ventas", ventaDAO.listarVentas());
        request.getRequestDispatcher("/jsp/ventas.jsp").forward(request, response);
    }
}
