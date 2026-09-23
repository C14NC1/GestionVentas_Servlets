<%@ page import="java.util.List" %>
<%@ page import="modelo.Cliente" %>
<%@ page import="modelo.Usuario" %>
<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    if (usuario == null) {
        usuario = (Usuario) session.getAttribute("usuario");
    }
    boolean esAdmin = usuario != null && "admin".equalsIgnoreCase(usuario.getRol());
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clientes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/estilos.css">
</head>
<body>
    <div class="wrap">
        <div class="topbar">
            <h2>Lista de clientes</h2>
            <a class="link" href="${pageContext.request.contextPath}/jsp/index.jsp">Volver al inicio</a>
        </div>

        <% if (esAdmin) { %>
        <div class="admin-panel">
            <h3>Agregar nuevo cliente</h3>
            <form class="admin-form" action="${pageContext.request.contextPath}/ClienteServlet" method="post">
                <input type="hidden" name="accion" value="agregar">
                <input type="text" name="nombre" placeholder="Nombre" required>
                <input type="email" name="correo" placeholder="Correo">
                <input type="tel" name="telefono" placeholder="Teléfono">
                <input type="text" name="direccion" placeholder="Dirección">
                <button type="submit" class="btn btn-primary">Guardar cliente</button>
            </form>
        </div>
        <% } %>

        <table class="data-table">
            <tr>
                <th>ID</th><th>Nombre</th><th>Correo</th><th>Teléfono</th><th>Dirección</th>
                <% if (esAdmin) { %><th>Acciones</th><% } %>
            </tr>
            <%
                if (clientes != null) {
                    for (Cliente c : clientes) {
            %>
            <tr>
                <td><%= c.getIdCliente() %></td>
                <td><%= c.getNombre() %></td>
                <td><%= c.getCorreo() %></td>
                <td><%= c.getTelefono() %></td>
                <td><%= c.getDireccion() %></td>
                <% if (esAdmin) { %>
                <td>
                    <form class="inline-form" action="${pageContext.request.contextPath}/ClienteServlet" method="post">
                        <input type="hidden" name="accion" value="eliminar">
                        <input type="hidden" name="id_cliente" value="<%= c.getIdCliente() %>">
                        <button type="submit" class="btn btn-danger">Eliminar</button>
                    </form>
                </td>
                <% } %>
            </tr>
            <%
                    }
                }
            %>
        </table>
    </div>
</body>
</html>
