package servlets;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.Usuario;

@WebFilter("/*")
public class SecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(javax.servlet.ServletRequest request,
            javax.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (!requiereSesion(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        Usuario usuario = session == null ? null : (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/api/")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write("{\"error\":\"autenticacion_requerida\"}");
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/login.jsp?error=login_required");
        }
    }

    private boolean requiereSesion(String path) {
        return "/ClienteServlet".equals(path)
                || "/ProductoServlet".equals(path)
                || "/VentaServlet".equals(path)
                || "/api/clientes".equals(path)
                || "/api/ventas".equals(path)
                || "/jsp/index.jsp".equals(path)
                || "/jsp/clientes.jsp".equals(path)
                || "/jsp/productos.jsp".equals(path)
                || "/jsp/ventas.jsp".equals(path);
    }

    @Override
    public void destroy() {}
}