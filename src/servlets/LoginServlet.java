package servlets;

import dao.UsuarioDAO;
import modelo.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Leer las credenciales enviadas por el usuario
        request.setCharacterEncoding("UTF-8");
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contrasena");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.validarLogin(correo, contraseña);

        if (usuario != null) {
            // Crear la sesión cuando el login es válido
            HttpSession sesion = request.getSession();
            sesion.setAttribute("usuario", usuario);
            response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
        } else {
            // Volver al login si las credenciales no coinciden
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp?error=1");
        }
    }
}
