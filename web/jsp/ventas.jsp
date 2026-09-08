<%@ page import="java.util.List" %>
<%@ page import="modelo.Venta" %>
<%
    List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ventas</title>
</head>
<body>
    <h2>Lista de Ventas</h2>
    <table border="1">
        <tr>
            <th>ID</th><th>Fecha</th><th>ID Cliente</th><th>ID Usuario</th><th>Total</th>
        </tr>
        <%
            if (ventas != null) {
                for (Venta v : ventas) {
        %>
        <tr>
            <td><%= v.getIdVenta() %></td>
            <td><%= v.getFecha() %></td>
            <td><%= v.getIdCliente() %></td>
            <td><%= v.getIdUsuario() %></td>
            <td><%= v.getTotal() %></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
