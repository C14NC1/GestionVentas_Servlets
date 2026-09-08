<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inicio - Gestión de Ventas</title>
</head>
<body>
    <h1>Bienvenido al sistema de Gestión de Ventas</h1>
    <nav>
        <a href="login.jsp">Iniciar sesión</a> | 
        <a href="registro.jsp">Registrarse</a> | 
        <a href="${pageContext.request.contextPath}/ClienteServlet">Ver clientes</a> | 
        <a href="${pageContext.request.contextPath}/ProductoServlet">Ver productos</a> | 
        <a href="${pageContext.request.contextPath}/VentaServlet">Ver ventas</a>
    </nav>
</body>
</html>
