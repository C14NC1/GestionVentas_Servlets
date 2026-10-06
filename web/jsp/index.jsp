<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inicio - Gestión de Ventas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/estilos.css">
</head>
<body class="dashboard-page">
    <div class="dashboard-shell">
        <header class="dashboard-header">
            <div>
                <span class="badge">Panel principal</span>
                <h1>Bienvenido a la cafetería</h1>
            </div>
        </header>

        <nav class="top-nav">
            <a href="login.jsp">Iniciar sesión</a>
            <a href="registro.jsp">Registrarse</a>
            <a href="${pageContext.request.contextPath}/ClienteServlet">Ver clientes</a>
            <a href="${pageContext.request.contextPath}/ProductoServlet">Ver productos</a>
            <a href="${pageContext.request.contextPath}/VentaServlet">Ver ventas</a>
            <form action="${pageContext.request.contextPath}/LogoutServlet" method="post">
                <button type="submit">Cerrar sesión</button>
            </form>
        </nav>

        <section class="info-box">
            <strong>Resumen:</strong> aquí podrás gestionar la venta de café y pan, revisar clientes, manejar productos y registrar ventas según el rol del usuario.
        </section>
    </div>
</body>
</html>
