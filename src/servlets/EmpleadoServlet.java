package servlets;

import dao.UsuarioDAO;
import modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmpleadoServlet")
public class EmpleadoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("empleados", new UsuarioDAO().listarEmpleados());
        request.getRequestDispatcher("/jsp/empleados.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        String accion = request.getParameter("accion");

        if ("agregar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String correo = request.getParameter("correo");
            String contraseña = request.getParameter("contrasena");
            if (nombre == null || nombre.trim().isEmpty() || correo == null || correo.trim().isEmpty()
                    || contraseña == null || contraseña.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/EmpleadoServlet?error=datos");
                return;
            }

            Usuario empleado = new Usuario(0, nombre.trim(), correo.trim(), contraseña, "vendedor");
            if (!usuarioDAO.insertarUsuario(empleado)) {
                String error = usuarioDAO.existeCorreo(correo.trim()) ? "correo" : "bd";
                response.sendRedirect(request.getContextPath() + "/EmpleadoServlet?error=" + error);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/EmpleadoServlet?resultado=agregado");
            return;
        }

        if ("eliminar".equals(accion)) {
            try {
                int idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
                String resultado = usuarioDAO.eliminarEmpleado(idUsuario) ? "eliminado" : "no_eliminado";
                response.sendRedirect(request.getContextPath() + "/EmpleadoServlet?resultado=" + resultado);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/EmpleadoServlet?resultado=no_eliminado");
            }
            return;
        }

        response.sendRedirect(request.getContextPath() + "/EmpleadoServlet");
    }
}