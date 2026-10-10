<%@ page import="java.util.List" %>
<%@ page import="modelo.Usuario" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Usuario> empleados = (List<Usuario>) request.getAttribute("empleados");
%>
<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Empleados</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/estilos.css">
</head>
<body>
    <div class="wrap">
        <div class="topbar">
            <h2>Empleados</h2>
            <a class="link" href="${pageContext.request.contextPath}/jsp/index.jsp">Volver al inicio</a>
        </div>

        <% if ("agregado".equals(request.getParameter("resultado"))) { %>
        <p role="status">Empleado registrado correctamente. Ya puede iniciar sesión.</p>
        <% } else if ("eliminado".equals(request.getParameter("resultado"))) { %>
        <p role="status">Empleado eliminado.</p>
        <% } else if ("no_eliminado".equals(request.getParameter("resultado"))) { %>
        <p role="alert">No se pudo eliminar el empleado. Puede tener ventas asociadas.</p>
        <% } else if ("datos".equals(request.getParameter("error"))) { %>
        <p role="alert">Completa el nombre, correo y contraseña.</p>
        <% } else if ("correo".equals(request.getParameter("error"))) { %>
        <p role="alert">Ese correo ya está registrado.</p>
        <% } else if ("bd".equals(request.getParameter("error"))) { %>
        <p role="alert">No se pudo guardar el empleado. Verifica la conexión con la base de datos.</p>
        <% } %>

        <div class="admin-panel">
            <h3>Agregar empleado</h3>
            <form class="admin-form" action="${pageContext.request.contextPath}/EmpleadoServlet" method="post">
                <input type="hidden" name="accion" value="agregar">
                <input type="text" name="nombre" placeholder="Nombre completo" maxlength="100" required>
                <input type="email" name="correo" placeholder="Correo" maxlength="100" required>
                <input type="password" name="contrasena" placeholder="Contraseña" required>
                <button type="submit" class="btn btn-primary">Guardar empleado</button>
            </form>
        </div>

        <table class="data-table">
            <tr><th>ID</th><th>Nombre</th><th>Correo</th><th>Acciones</th></tr>
            <% if (empleados != null && !empleados.isEmpty()) {
                for (Usuario empleado : empleados) { %>
            <tr>
                <td><%= empleado.getIdUsuario() %></td>
                <td><%= escapeHtml(empleado.getNombre()) %></td>
                <td><%= escapeHtml(empleado.getCorreo()) %></td>
                <td>
                    <form class="inline-form" action="${pageContext.request.contextPath}/EmpleadoServlet" method="post"
                            onsubmit="return confirm('¿Eliminar este empleado?');">
                        <input type="hidden" name="accion" value="eliminar">
                        <input type="hidden" name="id_usuario" value="<%= empleado.getIdUsuario() %>">
                        <button type="submit" class="btn btn-danger">Eliminar</button>
                    </form>
                </td>
            </tr>
            <%  }
               } else { %>
            <tr><td colspan="4">No hay empleados registrados.</td></tr>
            <% } %>
        </table>
    </div>
</body>
</html>