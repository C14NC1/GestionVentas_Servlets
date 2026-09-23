package servlets;

import dao.ClienteDAO;
import modelo.Cliente;
import modelo.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
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
        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientes = clienteDAO.listarClientes();
        request.setAttribute("clientes", clientes);
        request.setAttribute("usuario", obtenerUsuarioLogueado(request));
        request.getRequestDispatcher("/jsp/clientes.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (requiereLogin(request, response)) {
            return;
        }

        Usuario usuario = obtenerUsuarioLogueado(request);
        if (!"admin".equalsIgnoreCase(usuario.getRol())) {
            response.sendRedirect(request.getContextPath() + "/ClienteServlet?error=permiso");
            return;
        }

        String accion = request.getParameter("accion");
        ClienteDAO clienteDAO = new ClienteDAO();

        if ("eliminar".equals(accion)) {
            String idParam = request.getParameter("id_cliente");
            try {
                int idCliente = Integer.parseInt(idParam);
                clienteDAO.eliminarCliente(idCliente);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/ClienteServlet?error=datos");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
            return;
        }

        if (accion == null || "agregar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String correo = request.getParameter("correo");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");

            if (nombre == null || nombre.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/ClienteServlet?error=datos");
                return;
            }

            Cliente nuevo = new Cliente(0, nombre.trim(), correo, telefono, direccion);
            clienteDAO.insertarCliente(nuevo);
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/ClienteServlet?error=datos");
    }
}
