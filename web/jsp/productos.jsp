<%@ page import="java.util.List" %>
<%@ page import="modelo.Producto" %>
<%@ page import="modelo.Usuario" %>
<%
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
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
    <title>Productos</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f6f0ea; margin: 0; color: #2c1b12; }
        .wrap { max-width: 1100px; margin: 40px auto; padding: 0 20px 30px; }
        table { width: 100%; border-collapse: collapse; background: white; }
        th, td { border: 1px solid #dcc6b3; padding: 10px; text-align: left; }
        th { background: #f0d9c5; }
        .admin-panel { background: white; padding: 20px; border-radius: 12px; margin-bottom: 20px; box-shadow: 0 8px 16px rgba(0,0,0,0.06); }
        form { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 12px; }
        input, textarea, button { padding: 10px; border: 1px solid #d7c3ad; border-radius: 8px; }
        button { background: #8a4d26; color: white; font-weight: bold; cursor: pointer; }
        .danger { background: #a33d2e; }
        .topbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .link { text-decoration: none; color: #8a4d26; font-weight: bold; }
    </style>
</head>
<body>
    <div class="wrap">
        <div class="topbar">
            <h2>Productos en stock</h2>
            <a class="link" href="${pageContext.request.contextPath}/jsp/index.jsp">Volver al inicio</a>
        </div>

        <% if (esAdmin) { %>
        <div class="admin-panel">
            <h3>Agregar nuevo producto</h3>
            <form action="${pageContext.request.contextPath}/ProductoServlet" method="post">
                <input type="text" name="nombre" placeholder="Nombre" required>
                <input type="text" name="descripcion" placeholder="Descripción">
                <input type="number" step="0.01" min="0" name="precio" placeholder="Precio" required>
                <input type="number" min="0" name="stock" placeholder="Stock" required>
                <button type="submit">Guardar producto</button>
            </form>
        </div>
        <% } %>

        <table>
            <tr>
                <th>ID</th><th>Nombre</th><th>Descripción</th><th>Precio</th><th>Stock</th>
                <% if (esAdmin) { %><th>Acciones</th><% } %>
            </tr>
            <%
                if (productos != null) {
                    for (Producto p : productos) {
            %>
            <tr>
                <td><%= p.getIdProducto() %></td>
                <td><%= p.getNombre() %></td>
                <td><%= p.getDescripcion() %></td>
                <td>$<%= String.format("%.2f", p.getPrecio()) %></td>
                <td><%= p.getStock() %></td>
                <% if (esAdmin) { %>
                <td>
                    <form action="${pageContext.request.contextPath}/ProductoServlet" method="post" style="display:inline; grid-template-columns: none; gap: 0;">
                        <input type="hidden" name="accion" value="actualizar">
                        <input type="hidden" name="id_producto" value="<%= p.getIdProducto() %>">
                        <input type="text" name="nombre" value="<%= p.getNombre() %>" required style="width:90px; margin-bottom:5px;">
                        <input type="text" name="descripcion" value="<%= p.getDescripcion() %>" style="width:110px; margin-bottom:5px;">
                        <input type="number" step="0.01" min="0" name="precio" value="<%= p.getPrecio() %>" style="width:90px; margin-bottom:5px;">
                        <input type="number" min="0" name="stock" value="<%= p.getStock() %>" style="width:70px; margin-bottom:5px;">
                        <button type="submit">Editar</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/ProductoServlet" method="post" style="display:inline;">
                        <input type="hidden" name="accion" value="eliminar">
                        <input type="hidden" name="id_producto" value="<%= p.getIdProducto() %>">
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
