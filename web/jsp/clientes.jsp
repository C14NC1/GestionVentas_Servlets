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
    <style>
        body { font-family: Arial, sans-serif; background: #f8f2ee; margin: 0; color: #2c1b12; }
        .wrap { max-width: 1100px; margin: 40px auto; padding: 0 20px 30px; }
        table { width: 100%; border-collapse: collapse; background: white; }
        th, td { border: 1px solid #dcc6b3; padding: 10px; text-align: left; }
        th { background: #f0d9c5; }
        .admin-panel { background: white; padding: 20px; border-radius: 12px; margin-bottom: 20px; box-shadow: 0 8px 16px rgba(0,0,0,0.06); }
        form { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 12px; }
        input, button { padding: 10px; border: 1px solid #d7c3ad; border-radius: 8px; }
        button { background: #8a4d26; color: white; font-weight: bold; cursor: pointer; }
        .danger { background: #a33d2e; }
        .topbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .link { text-decoration: none; color: #8a4d26; font-weight: bold; }
    </style>
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
            <form action="${pageContext.request.contextPath}/ClienteServlet" method="post">
                <input type="text" name="nombre" placeholder="Nombre" required>
                <input type="email" name="correo" placeholder="Correo">
                <input type="tel" name="telefono" placeholder="Teléfono">
                <input type="text" name="direccion" placeholder="Dirección">
                <button type="submit">Guardar cliente</button>
            </form>
        </div>
        <% } %>

        <table>
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
                    <form action="${pageContext.request.contextPath}/ClienteServlet" method="post" style="display:inline; grid-template-columns: none; gap: 0;">
                        <input type="hidden" name="accion" value="eliminar">
                        <input type="hidden" name="id_cliente" value="<%= c.getIdCliente() %>">
                        <button type="submit" class="danger">Eliminar</button>
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
