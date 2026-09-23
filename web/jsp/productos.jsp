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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/estilos.css">
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
            <form class="admin-form" action="${pageContext.request.contextPath}/ProductoServlet" method="post">
                <input type="hidden" name="accion" value="agregar">
                <input type="text" name="nombre" placeholder="Nombre" required>
                <input type="text" name="descripcion" placeholder="Descripción">
                <input type="number" step="0.01" min="0" name="precio" placeholder="Precio" required>
                <input type="number" min="0" name="stock" placeholder="Stock" required>
                <button type="submit" class="btn btn-primary">Guardar producto</button>
            </form>
        </div>
        <% } %>

        <table class="data-table">
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
                    <div class="table-actions">
                        <form class="inline-form" action="${pageContext.request.contextPath}/ProductoServlet" method="post">
                            <input type="hidden" name="accion" value="actualizar">
                            <input type="hidden" name="id_producto" value="<%= p.getIdProducto() %>">
                            <input type="text" name="nombre" value="<%= p.getNombre() %>" required>
                            <input type="text" name="descripcion" value="<%= p.getDescripcion() %>">
                            <input type="number" step="0.01" min="0" name="precio" value="<%= p.getPrecio() %>">
                            <input type="number" min="0" name="stock" value="<%= p.getStock() %>">
                            <button type="submit" class="btn btn-primary">Editar</button>
                        </form>

                        <form class="inline-form" action="${pageContext.request.contextPath}/ProductoServlet" method="post">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="id_producto" value="<%= p.getIdProducto() %>">
                            <button type="submit" class="btn btn-danger">Eliminar</button>
                        </form>
                    </div>
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
