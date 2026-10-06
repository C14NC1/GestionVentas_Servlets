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
        request.setCharacterEncoding("UTF-8");
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contrasena");
        if (contraseña == null || contraseña.isEmpty()) {
            contraseña = request.getParameter("contraseña");
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.validarLogin(correo, contraseña);

        if (usuario != null) {
            HttpSession sesion = request.getSession();
            request.changeSessionId();
            sesion.setAttribute("usuario", usuario);
            response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp?error=1");
        }
    }
}
