package servlets;

import dao.ClienteDAO;
import modelo.Cliente;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientes = clienteDAO.listarClientes();
        request.setAttribute("clientes", clientes);
        request.getRequestDispatcher("/jsp/clientes.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");

        if (nombre == null || nombre.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/clientes.jsp?error=datos");
            return;
        }

        Cliente nuevo = new Cliente(0, nombre.trim(), correo, telefono, direccion);
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.insertarCliente(nuevo);

        response.sendRedirect(request.getContextPath() + "/ClienteServlet");
    }
}
