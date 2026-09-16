package servlets;

import dao.UsuarioDAO;
import modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/RegistroUsuarioServlet")
public class RegistroUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Leer los datos del formulario de registro
        request.setCharacterEncoding("UTF-8");
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contrasena");
        String rol = request.getParameter("rol");

        if (nombre == null || nombre.trim().isEmpty() || correo == null || correo.trim().isEmpty()
                || contraseña == null || contraseña.isEmpty() || rol == null || rol.trim().isEmpty()) {
            // Verificar que los campos obligatorios estén completos
            response.sendRedirect(request.getContextPath() + "/jsp/registro.jsp?error=datos");
            return;
        }

        Usuario nuevo = new Usuario(0, nombre.trim(), correo.trim(), contraseña, rol.trim());
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        // Registrar el usuario y controlar posibles errores de base de datos
        if (!usuarioDAO.insertarUsuario(nuevo)) {
            response.sendRedirect(request.getContextPath() + "/jsp/registro.jsp?error=bd");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp?registro=ok");
    }
}

