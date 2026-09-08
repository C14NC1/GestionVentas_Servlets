<%@ page import="java.util.List" %>
<%@ page import="modelo.Producto" %>
<%
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Productos</title>
</head>
<body>
    <h2>Lista de Productos</h2>
    <table border="1">
        <tr>
            <th>ID</th><th>Nombre</th><th>Descripción</th><th>Precio</th><th>Stock</th>
        </tr>
        <%
            if (productos != null) {
                for (Producto p : productos) {
        %>
        <tr>
            <td><%= p.getIdProducto() %></td>
            <td><%= p.getNombre() %></td>
            <td><%= p.getDescripcion() %></td>
            <td><%= p.getPrecio() %></td>
            <td><%= p.getStock() %></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
