<%@ page import="java.util.List" %>
<%@ page import="modelo.Cliente" %>
<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clientes</title>
</head>
<body>
    <h2>Lista de Clientes</h2>
    <table border="1">
        <tr>
            <th>ID</th><th>Nombre</th><th>Correo</th><th>Teléfono</th><th>Dirección</th>
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
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
